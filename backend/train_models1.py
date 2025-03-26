from sklearn.ensemble import RandomForestRegressor
from sklearn.tree import DecisionTreeRegressor
from sklearn.linear_model import LinearRegression
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
import pandas as pd
import joblib

# Load Dataset
df = pd.read_csv("wheat_dataset_crt.csv")

# Feature Engineering
df["Rainfall_Temperature"] = df["Rainfall"] * df["Temperature"]
df["Rainfall_Temperature_Ratio"] = df["Rainfall"] / (df["Temperature"] + 1)
df["SoilMoisture_Square"] = df["Soil_Moisture"] ** 2  # Use "Soil_Moisture"

# Select Features and Target
X = df.drop(columns=["Yield_kg_per_ha"])
y = df["Yield_kg_per_ha"]

# Train-Test Split
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# Feature Scaling
scaler = StandardScaler()
X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.transform(X_test)

# Train Random Forest Model
rf_model = RandomForestRegressor(n_estimators=300, max_depth=25, min_samples_split=4, min_samples_leaf=2, random_state=42)
rf_model.fit(X_train_scaled, y_train)

# Train Decision Tree Model
dt_model = DecisionTreeRegressor(max_depth=20, min_samples_split=5, random_state=42)
dt_model.fit(X_train_scaled, y_train)

# Train Linear Regression Model
lr_model = LinearRegression()
lr_model.fit(X_train_scaled, y_train)

# Save Models and Scaler
joblib.dump(rf_model, "models/random_forest1.pkl")
joblib.dump(dt_model, "models/decision_tree.pkl")
joblib.dump(lr_model, "models/linear_regression.pkl")
joblib.dump(scaler, "models/scaler.pkl")
joblib.dump(list(X.columns), "models/feature_names.pkl")  # Save feature names

print("🎯 All models trained and saved successfully!")
