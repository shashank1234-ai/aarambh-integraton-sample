from flask import Flask,request
from flask_cors import CORS
from common import send_settlement_to_agency
from aarambh_integration import create_settlement,update_settlement
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
    resp=update_settlement_data(request_payload)
    return resp

@app.route('/v1/on_recon',methods=['POST'])
def settlement_on_recon():
    request_payload = request.get_json()
    resp=update_settlement_data(request_payload)
    return resp


@app.route('/v1/on_settle',methods=['POST'])
def settlement_on_settle():
    request_payload = request.get_json()
    resp=update_settlement_data(request_payload)
    return resp

@app.route('/v1/on_report',methods=['POST'])
def settlement_on_report():
    request_payload = request.get_json()
    resp=update_settlement_data(request_payload)
    return resp


@app.route('/v1/transaction_recon')
def settlement_transaction_recon():
    request_payload = request.get_json()
    return send_retail_response_to_ondc_network(request_payload,'recon')

@app.route('/v1/on_transaction_recon')
def settlement_on_transaction_recon():
    request_payload = request.get_json()
    return send_retail_response_to_ondc_network(request_payload,'on_recon')
        


if __name__ == '__main__':
    app.run(host='0.0.0.0',port=8000)