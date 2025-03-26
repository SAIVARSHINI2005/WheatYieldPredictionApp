from flask import Flask, request, jsonify
import joblib
import pandas as pd
import numpy as np
import matplotlib
matplotlib.use('Agg')  # Use a non-GUI backend
import matplotlib.pyplot as plt
import io
import base64
import os
from dotenv import load_dotenv
import logging
from sklearn.metrics import r2_score, mean_absolute_error, mean_squared_error

# Load environment variables
load_dotenv()

app = Flask(__name__)
API_KEY = os.getenv("MyWeatherApp", "4b20902f7f1c7155ac22062487336862")

# Load models
rf_model = joblib.load("models/random_forest1.pkl")
dt_model = joblib.load("models/decision_tree.pkl")
lr_model = joblib.load("models/linear_regression.pkl")

# Load scaler and feature names
scaler = joblib.load("models/scaler.pkl")
feature_names = joblib.load("models/feature_names.pkl")

# Configure logging
logging.basicConfig(level=logging.DEBUG)

# Directory for saving charts
CHART_DIR = r"C:\Users\vvdsa\Downloads\WheatYieldPredictionApp\backend\csv_graphs"
os.makedirs(CHART_DIR, exist_ok=True)

def preprocess_data(df):
    """Preprocess CSV data by adding new features and scaling."""
    df["Rainfall_Temperature"] = df["Rainfall"] * df["Temperature"]
    df["Rainfall_Temperature_Ratio"] = df["Rainfall"] / (df["Temperature"] + 1)
    df["SoilMoisture_Square"] = df["Soil_Moisture"] ** 2

    missing_features = [col for col in feature_names if col not in df.columns]
    if missing_features:
        raise ValueError(f"Missing features in input data: {missing_features}")

    df = df[feature_names].dropna()
    return scaler.transform(df)

def evaluate_model(model, X, y, model_name):
    """Evaluate a model and return performance metrics."""
    preds = model.predict(X)
    rmse = np.sqrt(mean_squared_error(y, preds))
    mean_actual_yield = np.mean(y)
    accuracy = 100 * (1 - (rmse / mean_actual_yield)) if mean_actual_yield != 0 else 0

    return {
        "Model Name": model_name,
        "R2 Score": r2_score(y, preds),
        "Accuracy (%)": max(0, accuracy),
        "MAE": mean_absolute_error(y, preds),
        "RMSE": rmse,
        "Mean Actual Yield": mean_actual_yield,
        "Mean Predicted Yield": preds.mean(),
        "Predictions": preds.tolist()
    }

def evaluate_all_models(X, y):
    """Evaluate all models and find the best one."""
    models = {"Linear Regression": lr_model, "Random Forest": rf_model, "Decision Tree": dt_model}
    results = {}
    best_model = None
    best_accuracy = -1

    for name, model in models.items():
        try:
            results[name] = evaluate_model(model, X, y, name)
            if results[name]["Accuracy (%)"] > best_accuracy:
                best_accuracy = results[name]["Accuracy (%)"]
                best_model = name
        except Exception as e:
            results[name] = {"error": str(e)}

    results["Best Model"] = best_model if best_model else "Unknown"
    return results

def save_and_encode_plot(path):
    """Save and encode a plot as a Base64 string."""
    plt.savefig(path, format="png", bbox_inches="tight")
    plt.close()
    with open(path, "rb") as img_file:
        return base64.b64encode(img_file.read()).decode("utf-8")

def generate_feature_importance_plot(model, title, filename):
    """Generate feature importance plot for models that support it."""
    if hasattr(model, "feature_importances_"):
        plt.figure(figsize=(10, 5))
        plt.barh(feature_names, model.feature_importances_, color='skyblue')
        plt.xlabel("Importance Score")
        plt.ylabel("Features")
        plt.title(title)
        return save_and_encode_plot(os.path.join(CHART_DIR, filename))
    return None

def generate_scatter_plot(y_actual, y_pred, filename):
    """Generate Actual vs Predicted Scatter Plot."""
    plt.figure(figsize=(8, 5))
    plt.scatter(y_actual, y_pred, color='blue', alpha=0.6)
    plt.plot([min(y_actual), max(y_actual)], [min(y_actual), max(y_actual)], color="red", linestyle="--")
    plt.xlabel("Actual Yield")
    plt.ylabel("Predicted Yield")
    plt.title("Actual vs Predicted Yield")
    return save_and_encode_plot(os.path.join(CHART_DIR, filename))

def generate_residual_plot(y_actual, y_pred, filename):
    """Generate residual plot."""
    residuals = y_actual - np.array(y_pred)
    plt.figure(figsize=(8, 5))
    plt.scatter(y_pred, residuals, color='purple', alpha=0.6)
    plt.axhline(y=0, color="black", linestyle="--")
    plt.xlabel("Predicted Yield")
    plt.ylabel("Residuals")
    plt.title("Residual Plot")
    return save_and_encode_plot(os.path.join(CHART_DIR, filename))

def generate_performance_comparison(models, filename):
    """Generate bar chart comparing model RMSE and Accuracy."""
    labels = list(models.keys())
    rmse_values = [models[m]["RMSE"] for m in labels]
    accuracy_values = [models[m]["Accuracy (%)"] for m in labels]

    fig, ax1 = plt.subplots(figsize=(10, 5))

    ax1.set_xlabel("Model")
    ax1.set_ylabel("RMSE", color="tab:red")
    ax1.bar(labels, rmse_values, color="tab:red", alpha=0.6)
    
    ax2 = ax1.twinx()
    ax2.set_ylabel("Accuracy (%)", color="tab:blue")
    ax2.plot(labels, accuracy_values, color="tab:blue", marker="o", linestyle="--")

    plt.title("Model Performance Comparison")
    return save_and_encode_plot(os.path.join(CHART_DIR, filename))

@app.route('/evaluate_csv', methods=['POST'])
def evaluate_csv():
    """Handle CSV file evaluation."""
    if request.headers.get("Authorization") != f"Bearer {API_KEY}":
        return jsonify({"error": "Unauthorized"}), 401

    try:
        file = request.files['file']
        df = pd.read_csv(file)

        X, y = preprocess_data(df.iloc[:, :-1]), df.iloc[:, -1]
        performance = evaluate_all_models(X, y)
        best_model_name = performance.pop("Best Model")
        best_model_preds = performance[best_model_name]["Predictions"]

        # Generate plots
        scatter_plot = generate_scatter_plot(y, best_model_preds, "scatter_plot.png")
        residual_plot = generate_residual_plot(y, best_model_preds, "residual_plot.png")
        performance_chart = generate_performance_comparison(performance, "performance_chart.png")

        fi_plot_rf = generate_feature_importance_plot(rf_model, "Random Forest Feature Importance", "rf_importance.png")
        fi_plot_dt = generate_feature_importance_plot(dt_model, "Decision Tree Feature Importance", "dt_importance.png")

        return jsonify({
            "best_model": best_model_name,
            "model_performance": performance,
            "scatter_plot": scatter_plot,
            "residual_plot": residual_plot,
            "performance_chart": performance_chart,
            "feature_importance_rf": fi_plot_rf,
            "feature_importance_dt": fi_plot_dt
        })

    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5001, debug=True)
