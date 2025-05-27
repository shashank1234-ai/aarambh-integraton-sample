import http from 'http';
import express from 'express';
import path from 'path';
import cors from 'cors';
import bodyParser from 'body-parser';
import { sendSettlementToAgency, postOnBapBpp } from './utils/common.js';
import { createRecord, updateRecord } from './utils/aarambhIntegration.js';

const app = express();

// Middleware
app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(bodyParser.json());

// Settlement endpoint
app.post('/v1/settle', async (req, res) => {
    try {
        const [response, statusCode] = await sendSettlementToAgency(req.body, 'settle');
        res.status(statusCode).send(response);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// Report endpoint
app.post('/v1/report', async (req, res) => {
    try {
        const [response, statusCode] = await sendSettlementToAgency(req.body, 'report');
        res.status(statusCode).send(response);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// Recon endpoint
app.post('/v1/recon', async (req, res) => {
    try {
        const requestType = req.headers.requesttype;
        let response;
        if (requestType) {
            response = await postOnBapBpp(req.body);
        } else {
            response = await updateRecord({ type: 'RECON', data: req.body });
        }
        res.json(response);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// On Recon endpoint
app.post('/v1/on_recon', async (req, res) => {
    try {
        const requestType = req.headers.requesttype;
        let response;
        if (requestType) {
            response = await postOnBapBpp(req.body);
        } else {
            response = await updateRecord({ type: 'ON_RECON', data: req.body });
        }
        res.json(response);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// On Settle endpoint
app.post('/v1/on_settle', async (req, res) => {
    try {
        const response = await updateRecord({ type: 'ON_SETTLE', data: req.body });
        res.json(response);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// On Report endpoint
app.post('/v1/on_report', async (req, res) => {
    try {
        const response = await updateRecord({ type: 'ON_REPORT', data: req.body });
        res.json(response);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// Error handling middleware
app.use((err, req, res, next) => {
    console.error(err.stack);
    
    // Send response status based on custom error code
    if (err.status) {
        return res.status(err.status).json({ error: err.message });
    }

    // If no custom error is thrown then return 500(server side error/exception)
    res.status(500).json({ error: 'Something went wrong. Please try again' });
});

// 404 handler
app.use((req, res) => {
    res.status(404).json({ error: 'Route not found' });
});

const PORT = process.env.PORT || 8000;
const server = http.createServer(app);

server.listen(PORT, '0.0.0.0', () => {
    console.log(`Server is running on port ${PORT}`);
});

export default app;