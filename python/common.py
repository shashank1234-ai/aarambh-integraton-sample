from nacl.bindings import crypto_sign_ed25519_sk_to_seed
import base64
import datetime
import nacl.encoding
import nacl.hash
from nacl.signing import SigningKey
import json
import requests
from config import *

def send_settlement_to_agency(payload,request_type):
    sa_end_point = f"{payload['context']['bpp_uri']}/{request_type}"
    resp,status_code = make_request_over_sa(payload,SA_END_POINT)
    return resp,status_code

def make_request_over_sa(payload,end_point):
    auth_header = create_authorisation_header(payload)
    headers={}
    headers['Authorization'] = auth_header
    response, status_code = post_on_bg_or_bap(end_point, payload, headers=headers)
    return response, status_code

def post_on_bg_or_bap(url, payload, headers={}):
    headers.update({'Content-Type': 'application/json'})
    raw_data = json.dumps(payload,separators=(',', ':'))
    response = requests.post(url, data=raw_data, headers=headers)
    return response.text, response.status_code

def create_signing_string(digest_base64, created=None, expires=None):
    if created is None:
        created = int(datetime.datetime.now().timestamp())
    if expires is None:
        expires = int((datetime.datetime.now() + datetime.timedelta(hours=1)).timestamp())
    signing_string = f"""(created): {created}
(expires): {expires}
digest: BLAKE-512={digest_base64}"""
    return signing_string

def sign_response(signing_key, private_key):
    private_key64 = base64.b64decode(private_key)
    seed = crypto_sign_ed25519_sk_to_seed(private_key64)
    signer = SigningKey(seed)
    signed = signer.sign(bytes(signing_key, encoding='utf8'))
    signature = base64.b64encode(signed.signature).decode()
    return signature


def create_authorisation_header(request_body, created=None, expires=None):
    created = int(datetime.datetime.now().timestamp()) if created is None else created
    expires = int((datetime.datetime.now() + datetime.timedelta(hours=1)).timestamp()) if expires is None else expires
    signing_key = create_signing_string(hash_message(json.dumps(request_body,separators=(',', ':'))),
                                        created=created, expires=expires)
    signature = sign_response(signing_key, private_key=PRIVATE_KEY)

    subscriber_id = SUBSCRIBER_ID
    unique_key_id = UNIQUE_KEY
    header = f'Signature keyId="{subscriber_id}|{unique_key_id}|ed25519",algorithm="ed25519",created=' \
             f'"{created}",expires="{expires}",headers="(created) (expires) digest",signature="{signature}"'
    return header
