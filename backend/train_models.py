import numpy as np
import joblib
from sklearn.linear_model import LinearRegression
from sklearn.ensemble import RandomForestRegressor
from sklearn.tree import DecisionTreeRegressor

# Dummy historical data (Replace this with actual training logic)
np.random.seed(42)
X_train = np.random.rand(100, 5) * 100
y_train = np.random.rand(100) * 50

# Train models
lr_model = LinearRegression()
rf_model = RandomForestRegressor(n_estimators=100, random_state=42)
dt_model = DecisionTreeRegressor(random_state=42)

lr_model.fit(X_train, y_train)
rf_model.fit(X_train, y_train)
dt_model.fit(X_train, y_train)

# Save models
joblib.dump(lr_model, 'models/linear_regression.pkl')
joblib.dump(rf_model, 'models/random_forest.pkl')
joblib.dump(dt_model, 'models/decision_tree.pkl')

print("Models trained and saved successfully!")
 
