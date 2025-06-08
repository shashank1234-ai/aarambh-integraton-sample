package ondc.onboarding.utility;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;


@RestController
public class Routes extends  Utils{
    private String signMessage;

    @Autowired
    private Map<String,byte[]> keys;

    @Autowired
    private String ondcPublicKey;

    @Autowired
    private String vlookupUrl;

    @Autowired
    private String requestId;

    @Autowired
    private String gatewayUrl;
    private final Logger logger =  LoggerFactory.getLogger(Routes.class);;


    @PostMapping("/create-header")
    public
    String createHeader(@RequestBody JsonNode req) throws Exception {
        long created = System.currentTimeMillis() / 1000L;
        long expires = created + 300000;
        logger.info(toBase64(generateBlakeHash(req.get("value").toString())));
        logger.info(req.get("value").toString());
        String hashedReq = hashMassage(req.get("value").toString(),created,expires);
        String signature = sign(Base64.getDecoder().decode(req.get("private_key").asText()),hashedReq.getBytes());
        String subscriberId = req.get("subscriber_id").asText();
        String uniqueKeyId = req.get("unique_key_id").asText();

        return "Signature keyId=\"" + subscriberId + "|" + uniqueKeyId + "|" + "ed25519\"" + ",algorithm=\"ed25519\"," + "created=\"" + created + "\",expires=\"" + expires + "\",headers=\"(created) (expires)" + " digest\",signature=\"" + signature + "\"";
    }

    @PostMapping("/verify-header")
    public boolean isValidHeader(@RequestBody JsonNode req) throws Exception {
        long currentTimestamp = System.currentTimeMillis() / 1000L;
        String authHeader = req.get("header").asText();
        String signature = authHeader.split(",")[5].split("=")[1].replaceAll("\"","");
        long expires = Long.parseLong(authHeader.split(",")[3].split("=")[1].replaceAll("\"",""));
        long created = Long.parseLong(authHeader.split(",")[2].split("=")[1].replaceAll("\"",""));
        if ((created > currentTimestamp) || currentTimestamp > expires){
            logger.info("Timestamp should be Created < CurrentTimestamp < Expires");
            return false;
        }
        String hashedReq = hashMassage(req.get("value").toString(),created,expires);
        logger.info(hashedReq);
        return verify(
                fromBase64(signature),
                hashedReq.getBytes(),
                fromBase64(req.get("public_key").asText())
        );
    }
}