import requests

# Define API endpoint
url = "http://127.0.0.1:5000/predict"

# Replace this with your actual API key
API_KEY = "your_secret_api_key"

headers = {
    "Authorization": f"Bearer {API_KEY}",
    "Content-Type": "application/json"
}

data = {
    "rainfall": 50,
    "temperature": 25,
    "nitrogen": 30,
    "phosphorus": 20,
    "potassium": 15
}

response = requests.post(url, json=data, headers=headers)
print("Response Status Code:", response.status_code)
print("Response JSON:", response.json())
