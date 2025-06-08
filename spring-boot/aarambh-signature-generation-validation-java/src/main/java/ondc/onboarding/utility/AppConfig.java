package ondc.onboarding.utility;

import org.json.JSONException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfig {
    @Bean
    public Map<String, byte[]> keys() {
        Map<String, byte[]> keys = new HashMap<>();
        // Initialize with empty byte arrays
        keys.put("sign_public_key", new byte[0]);
        keys.put("sign_private_key", new byte[0]);
        keys.put("enc_public_key", new byte[0]);
        keys.put("enc_private_key", new byte[0]);
        return keys;
    }

    @Bean
    public String requestId() {
        return "";
    }

    @Bean
    public String ondcPublicKey() {
        return "MCowBQYDK2VuAyEAduMuZgmtpjdCuxv+Nc49K0cB6tL/Dj3HZetvVN7ZekM=";
    }

    @Bean
    public String gatewayUrl() {
        return "https://staging.registry.ondc.org/subscribe";
    }

    @Bean
    public String vlookupUrl() {
        //Staging
        return "https://staging.registry.ondc.org/vlookup";

        //Preprod
        //return "https://preprod.registry.ondc.org/ondc/vlookup";

        //Prod
        //return "https://prod.registry.ondc.org/vlookup";
    }
}

