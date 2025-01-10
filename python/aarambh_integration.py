from config import *
import requests
import json

def create_settlement(on_confirm_payload):
    print(on_confirm)
    headers = {
        'x-access-key':AARAMBH_ACCESS,
        'x-secret-key':AARAMBH_SECRET,
        'subscriberid':SUBSCRIBER_ID
    }
    request_uri = f"{AARAMBH_INTEGRATION_URI}/create_settlement"
    cancellation_terms = on_confirm['message']['order']['cancellation_terms'] if on_confirm['message']['order']['cancellation_terms'] else []
    np_type = NP_TYPE
    payload = {
        'on_confirm':on_confirm,
        'cancellation_terms':cancellation_terms,
        # 'np_type':np_type
    }
    response = requests.post(url, data=json.dumps(payload), headers=headers)
    return response.text

def update_settlement(payload):
    headers = {
        'x-access-key':AARAMBH_ACCESS,
        'x-secret-key':AARAMBH_SECRET,
        'subscriberid':SUBSCRIBER_ID
    }
    np_type = NP_TYPE
    request_payload = {
        'update_data':payload,
        'request_type':payload['context']['action']
        # 'order_id':payload['context']['action']payload['message']['order']['id'],
        # 'np_type':np_type
    }
    request_uri = f"{AARAMBH_INTEGRATION_URI}/update_settlement"
    response = requests.post(url, data=json.dumps(request_payload), headers=headers)
    return response.text
    
  