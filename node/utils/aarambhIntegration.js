import axios from 'axios';
import dotenv from 'dotenv';
import  createAarambhAuthHeader  from './auth';
dotenv.config();

const AARAMBH_ACCESS = process.env.AARAMBH_ACCESS;
const AARAMBH_SECRET = process.env.AARAMBH_SECRET;
const AARAMBH_INTEGRATION_URI = process.env.AARAMBH_INTEGRATION_URI || 'https://integrations.aarambh.cloud/api/v1/integrations';
const SUBSCRIBER_ID = process.env.SUBSCRIBER_ID

async function createRecord(data) {
    try {
        const response = await axios.post(AARAMBH_INTEGRATION_URI, data, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization':createAarambhAuthHeader(data['data']),
                'subscriberid':SUBSCRIBER_ID
            }
        });
        return response.data;
    } catch (error) {
        throw new Error(`Failed to create record: ${error.message}`);
    }
}

async function updateRecord(data) {
    try {
        const response = await axios.put(AARAMBH_INTEGRATION_URI, data, {
           headers: {
                'Content-Type': 'application/json',
                'Authorization':createAarambhAuthHeader(data['data']),
                'subscriberid':SUBSCRIBER_ID
            }
        });
        return response.data;
    } catch (error) {
        throw new Error(`Failed to update record: ${error.message}`);
    }
}

export {
    createRecord,
    updateRecord
}; 