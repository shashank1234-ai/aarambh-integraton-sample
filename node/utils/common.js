import axios from 'axios';
import tweetnacl from 'tweetnacl';
import blake from 'blakejs';
import { createAuthHeader } from './auth.js';
import dotenv from 'dotenv';

dotenv.config();

const SA_END_POINT = process.env.SA_END_POINT;

async function sendSettlementToAgency(payload, requestType) {
    const saEndpoint = `${payload.context.bpp_uri}/${requestType}`;
    return makeRequestOverSA(payload, SA_END_POINT);
}

async function makeRequestOverSA(payload, endpoint) {
    const authHeader = await createAuthHeader(payload);
    const headers = {
        'Authorization': authHeader,
        'Content-Type': 'application/json'
    };
    return postOnBgOrBap(endpoint, payload, headers);
}

async function postOnBgOrBap(url, payload, headers = {}) {
    try {
        const response = await axios.post(url, payload, { headers });
        return [response.data, response.status];
    } catch (error) {
        throw new Error(`Request failed: ${error.message}`);
    }
}

async function postOnBapBpp(requestPayload) {
    const authHeader = await createAuthHeader(requestPayload);
    const requestUri = ''; // Extract request URI based on type of NP
    const headers = { 'Authorization': authHeader };
    return postOnBgOrBap(requestUri, requestPayload, headers);
}

export {
    sendSettlementToAgency,
    makeRequestOverSA,
    postOnBgOrBap,
    postOnBapBpp
};
