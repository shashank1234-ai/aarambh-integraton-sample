// import tweetnacl from 'tweetnacl';
import { createAuthorizationHeader } from "ondc-crypto-sdk-nodejs"
import blake from 'blakejs';
import dotenv from 'dotenv';

dotenv.config();

const UNIQUE_KEY = process.env.UNIQUE_KEY;
const SUBSCRIBER_ID = process.env.SUBSCRIBER_ID;
const PRIVATE_KEY = process.env.PRIVATE_KEY;
const AARAMBH_PRIVATE_KEY = process.env.AARAMBH_SECRET

async function createAuthHeader(requestBody, created = null, expires = null) {
    const authHeader = await createAuthorizationHeader({
      body: JSON.stringify(requestBody),
      privateKey: PRIVATE_KEY,
      subscriberId: SUBSCRIBER_ID, // Subscriber ID that you get after registering to ONDC Network
      subscriberUniqueKeyId: UNIQUE_KEY, // Unique Key Id or uKid that you get after registering to ONDC Network
    })
    return authHeader;
}

async function createAarambhAuthHeader(requestBody){
    const authHeader = await createAuthorizationHeader({
      body: JSON.stringify(requestBody),
      privateKey: AARAMBH_PRIVATE_KEY,
      subscriberId: SUBSCRIBER_ID, // Subscriber ID that you get after registering to ONDC Network
      subscriberUniqueKeyId: UNIQUE_KEY, // Unique Key Id or uKid that you get after registering to ONDC Network
    })
    return authHeader;
}
export {
    createAuthHeader,
    createAarambhAuthHeader
}; 
