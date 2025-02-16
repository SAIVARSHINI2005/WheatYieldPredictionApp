from flask import Flask, request, jsonify
import joblib
import os
import numpy as np
from sklearn.metrics import r2_score

app = Flask(__name__)

print("Starting Flask server...")

# Load trained models
lr_model = joblib.load("models/linear_regression.pkl")
rf_model = joblib.load("models/random_forest.pkl")
dt_model = joblib.load("models/decision_tree.pkl")

# Dummy test data to calculate model accuracy (replace with real test data)
X_test = np.random.rand(20, 5) * 100  # 20 test samples with 5 features
y_test = np.random.rand(20) * 50  # Actual yield values

# Get model predictions
lr_pred_test = lr_model.predict(X_test)
rf_pred_test = rf_model.predict(X_test)
dt_pred_test = dt_model.predict(X_test)

# Calculate R² score (accuracy) for each model
lr_accuracy = r2_score(y_test, lr_pred_test)
rf_accuracy = r2_score(y_test, rf_pred_test)
dt_accuracy = r2_score(y_test, dt_pred_test)

# Debug: Print model accuracies
print(f"Linear Regression Accuracy: {lr_accuracy:.4f}")
print(f"Random Forest Accuracy: {rf_accuracy:.4f}")
print(f"Decision Tree Accuracy: {dt_accuracy:.4f}")

# Select best model based on highest accuracy
model_accuracies = {
    "Linear Regression": lr_accuracy,
    "Random Forest": rf_accuracy,
    "Decision Tree": dt_accuracy
}
best_model = max(model_accuracies, key=model_accuracies.get)
print(f"Best Model Based on Accuracy: {best_model}")

@app.route('/predict', methods=['POST'])
def predict():
    try:
        print("Received prediction request...")
        data = request.json  # Receive JSON input from Android
        features = np.array([
            data["rainfall"], 
            data["temperature"], 
            data["nitrogen"], 
            data["phosphorus"], 
            data["potassium"]
        ]).reshape(1, -1)

        # Predict using all models
        lr_pred = lr_model.predict(features)[0]
        rf_pred = rf_model.predict(features)[0]
        dt_pred = dt_model.predict(features)[0]

        # Debug: Print predictions to check if they are working
        print(f"Linear Regression Prediction: {lr_pred}")
        print(f"Random Forest Prediction: {rf_pred}")
        print(f"Decision Tree Prediction: {dt_pred}")

        # Store predictions in a dictionary
        predictions = {
            "Linear Regression": lr_pred,
            "Random Forest": rf_pred,
            "Decision Tree": dt_pred
        }

        # Respond with predictions and best model
        response = {
            "predictions": predictions,
            "model_accuracies": model_accuracies,
            "best_model": best_model
        }
        print("Sending response:", response)
        print("Prediction request completed.\n")

        return jsonify(response)

    except Exception as e:
        print(f"Error: {str(e)}")
        return jsonify({"error": str(e)})



if __name__ == "__main__":
    port = int(os.environ.get("PORT", 5000))  # Get PORT from Render
    app.run(host="0.0.0.0", port=port)  # Allow external connections

