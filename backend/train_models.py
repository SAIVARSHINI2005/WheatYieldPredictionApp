
import numpy as np
import joblib
from sklearn.linear_model import LinearRegression
from sklearn.ensemble import RandomForestRegressor
from sklearn.tree import DecisionTreeRegressor

# Set random seed for reproducibility
np.random.seed(42)

# Generate synthetic correlated dataset (Modify this with real data if available)
X_train = np.random.rand(1000, 5) * 100  # 1000 samples, 5 features
y_train = (
    (3 * X_train[:, 0]) +  # Rainfall
    (2.5 * X_train[:, 1]) +  # Temperature
    (1.8 * X_train[:, 2]) +  # Nitrogen
    (2.2 * X_train[:, 3]) +  # Phosphorus
    (1.5 * X_train[:, 4]) +  # Potassium
    np.random.rand(1000) * 10  # Noise
)

# Train models
lr_model = LinearRegression()
rf_model = RandomForestRegressor(n_estimators=200, max_depth=10, random_state=42)
dt_model = DecisionTreeRegressor(max_depth=5, min_samples_split=4, random_state=42)

lr_model.fit(X_train, y_train)
rf_model.fit(X_train, y_train)
dt_model.fit(X_train, y_train)

# Save trained models
joblib.dump(lr_model, 'models/linear_regression.pkl')
joblib.dump(rf_model, 'models/random_forest.pkl')
joblib.dump(dt_model, 'models/decision_tree.pkl')

print("✅ Models trained and saved successfully!")
