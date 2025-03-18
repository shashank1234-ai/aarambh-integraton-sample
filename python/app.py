from flask import Flask,request
from flask_cors import CORS
from common import send_settlement_to_agency,post_on_bap_bpp
from aarambh_integration import create_record,update_record
app = Flask(__name__)
CORS(app)
@app.route('/v1/settle',methods=['POST'])
def settle_transaction():
    request_payload = request.get_json()
    return send_settlement_to_agency(request_payload,'settle')

@app.route('/v1/report',methods=['POST'])
def settlement_report():
    request_payload = request.get_json()
    return send_settlement_to_agency(request_payload,'report')

@app.route('/v1/recon',methods=['POST'])
def settlement_recon():

    request_payload = request.get_json()
    requesttype = request.headers.get('requesttype') #To check whether it is INBOUND or OUTBOUD request
    if requesttype:
        resp = post_on_bap_bpp(request_payload)
    else:
        resp=update_record({'type':'RECON','data':request_payload})
    return resp

@app.route('/v1/on_recon',methods=['POST'])
def settlement_on_recon():
    request_payload = request.get_json()
    requesttype = request.headers.get('requesttype') #To check whether it is INBOUND or OUTBOUD request
    if requesttype:
        resp = post_on_bap_bpp(request_payload)
    else:
        resp=update_record({'type':'ON_RECON','data':request_payload})
    return resp


@app.route('/v1/on_settle',methods=['POST'])
def settlement_on_settle():
    request_payload = request.get_json()
    resp=update_record({'type':'ON_SETTLE','data':request_payload})
    return resp

@app.route('/v1/on_report',methods=['POST'])
def settlement_on_report():
    request_payload = request.get_json()
    resp=update_record({'type':'ON_REPORT','data':request_payload})
    return resp



if __name__ == '__main__':
    app.run(host='0.0.0.0',port=8000)