import pandas as pd
import xgboost as xgb
import pickle
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error, r2_score, mean_absolute_error
from sklearn.preprocessing import StandardScaler
import numpy as np

# Load dataset
file_path = "C:/Users/vvdsa/Downloads/WheatYieldPredictionApp/backend/wheat_dataset_crt.csv"
df = pd.read_csv(file_path)
df.columns = df.columns.str.strip().str.lower()

y_column = 'yield_kg_per_ha'
X = df.drop(columns=[y_column])
y = df[y_column]

# Feature Scaling
scaler = StandardScaler()
X_scaled = scaler.fit_transform(X)

# Split data
X_train, X_test, y_train, y_test = train_test_split(X_scaled, y, test_size=0.2, random_state=42)

# Train Optimized XGBoost Model
model = xgb.XGBRegressor(
    objective='reg:squarederror', 
    n_estimators=600,  # Increased for better learning
    learning_rate=0.02,  # Reduced to improve generalization
    max_depth=13,  # Increased to capture more patterns
    subsample=0.95,  # Slightly higher for better learning
    colsample_bytree=0.85,  # Improved feature selection
    random_state=42
)
model.fit(X_train, y_train)

# Model Evaluation
y_pred = model.predict(X_test)
r2 = r2_score(y_test, y_pred)
mse = mean_squared_error(y_test, y_pred)
rmse = np.sqrt(mse)
mae = mean_absolute_error(y_test, y_pred)
accuracy = round((1 - (rmse / y_test.mean())) * 100, 2)

print(f"Model Performance: R² = {r2:.4f}, RMSE = {rmse:.2f}, MAE = {mae:.2f}, Accuracy = {accuracy}%")

# Save the model, scaler, and feature names
with open("wheat_yield_model.pkl", "wb") as f:
    pickle.dump(model, f)
with open("scaler.pkl", "wb") as f:
    pickle.dump(scaler, f)
with open("feature_names.pkl", "wb") as f:
    pickle.dump(X.columns.tolist(), f)

print("✅ Model, scaler, and feature names saved!")
