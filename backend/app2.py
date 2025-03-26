import matplotlib
matplotlib.use('Agg')  # Use non-interactive backend to avoid Tkinter error

from flask import Flask, request, jsonify, send_from_directory
import pandas as pd
import pickle
import numpy as np
import matplotlib.pyplot as plt
import xgboost as xgb
from sklearn.metrics import r2_score, mean_absolute_error, mean_squared_error
import os

# Load model, scaler, and feature names
with open("wheat_yield_model.pkl", "rb") as f:
    model = pickle.load(f)
with open("scaler.pkl", "rb") as f:
    scaler = pickle.load(f)
with open("feature_names.pkl", "rb") as f:
    feature_names = pickle.load(f)

# Define directory to save graphs
graph_dir = os.path.join(os.getcwd(), "XgBoost_graphs")
os.makedirs(graph_dir, exist_ok=True)

app = Flask(__name__)
app.config["GRAPH_FOLDER"] = graph_dir

@app.route("/graphs/<filename>")
def get_graph(filename):
    """ Serve graphs to Android App via URL """
    return send_from_directory(app.config["GRAPH_FOLDER"], filename)

@app.route("/predictxgboost", methods=["POST"])
def predict_xgboost():
    try:
        file = request.files["file"]
        df = pd.read_csv(file)
        df.columns = df.columns.str.lower()

        # Ensure required features exist in input
        missing_features = [feat for feat in feature_names if feat not in df.columns]
        if missing_features:
            return jsonify({"error": f"Missing features: {missing_features}"}), 400

        X = df[feature_names]
        X_scaled = scaler.transform(X)
        y_pred = model.predict(X_scaled)

        predicted_yield = np.clip(y_pred.mean(), 5000, 7000)

        # Calculate metrics if 'yield' column exists
        if 'yield_kg_per_ha' in df.columns:
            y_true = df['yield_kg_per_ha']
            r2 = round(r2_score(y_true, y_pred), 2)
            rmse = round(np.sqrt(mean_squared_error(y_true, y_pred)), 2)
            mae = round(mean_absolute_error(y_true, y_pred), 2)
            accuracy = round((1 - (rmse / y_true.mean())) * 100, 2)
        else:
            r2, rmse, mae, accuracy = None, None, None, None

        # Generate and save Feature Importance Plot
        plt.figure(figsize=(10, 6))
        xgb.plot_importance(model, importance_type='weight')
        feature_importance_path = "feature_importance.png"
        plt.savefig(os.path.join(graph_dir, feature_importance_path))
        plt.close()

        # Generate and save Actual vs Predicted Scatter Plot
        if 'yield_kg_per_ha' in df.columns:
            plt.figure(figsize=(8, 6))
            plt.scatter(y_true, y_pred, alpha=0.5)
            plt.xlabel("Actual Yield")
            plt.ylabel("Predicted Yield")
            plt.title("Actual vs Predicted Yield")
            actual_vs_predicted_path = "actual_vs_predicted.png"
            plt.savefig(os.path.join(graph_dir, actual_vs_predicted_path))
            plt.close()

            # Generate and save Residual Plot
            residuals = y_true - y_pred
            plt.figure(figsize=(8, 6))
            plt.scatter(y_pred, residuals, alpha=0.5)
            plt.axhline(y=0, color='r', linestyle='--')
            plt.xlabel("Predicted Yield")
            plt.ylabel("Residuals")
            plt.title("Residual Plot")
            residual_plot_path = "residual_plot.png"
            plt.savefig(os.path.join(graph_dir, residual_plot_path))
            plt.close()
        else:
            actual_vs_predicted_path, residual_plot_path = None, None

        # Generate and save XGBoost Tree Visualization
        plt.figure(figsize=(12, 8))
        plt.rcParams.update({'font.size': 14})  # Increase font size

        xgb.plot_tree(model, num_trees=0)
        xgboost_tree_path = "xgboost_tree.png"
        plt.savefig(os.path.join(graph_dir, xgboost_tree_path))
        plt.close()

        # Response with URLs instead of local paths
        response = {
            "predicted_yield_kg_ha": float(round(predicted_yield, 2)),
            "r2_score": r2 if r2 is not None else "N/A",
            "rmse": rmse if rmse is not None else "N/A",
            "mae": mae if mae is not None else "N/A",
            "accuracy": accuracy if accuracy is not None else "N/A",
            "graphs": {
                "feature_importance": f"https://192a-2401-4900-6073-bf4c-55c7-c163-a0ed-ee8f.ngrok-free.app/graphs/{feature_importance_path}",
                "actual_vs_predicted": f"https://192a-2401-4900-6073-bf4c-55c7-c163-a0ed-ee8f.ngrok-free.app/graphs/{actual_vs_predicted_path}" if actual_vs_predicted_path else "N/A",
                "residual_plot": f"https://192a-2401-4900-6073-bf4c-55c7-c163-a0ed-ee8f.ngrok-free.app/graphs/{residual_plot_path}" if residual_plot_path else "N/A",
                "xgboost_tree": f"https://192a-2401-4900-6073-bf4c-55c7-c163-a0ed-ee8f.ngrok-free.app/graphs/{xgboost_tree_path}"
            }
        }
        return jsonify(response)

    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True, threaded=False)  # Ensure single-threaded execution
