from config import *
import requests
import json

def create_record(payload):
    headers = {
        'x-access-key':AARAMBH_ACCESS,
        'x-secret-key':AARAMBH_SECRET,
        'subscriberid':SUBSCRIBER_ID
    }
    request_uri = f"{AARAMBH_INTEGRATION_URI}/create_record"    
    response = requests.post(request_uri, data=json.dumps(payload), headers=headers)
    return response.text

def update_record(payload):
    headers = {
        'x-access-key':AARAMBH_ACCESS,
        'x-secret-key':AARAMBH_SECRET,
        'subscriberid':SUBSCRIBER_ID
    }
    
    request_uri = f"{AARAMBH_INTEGRATION_URI}/update_record"
    response = requests.post(request_uri, data=json.dumps(payload), headers=headers)
    return response.text
    
  