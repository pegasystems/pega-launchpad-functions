package com.pega.launchpad.docusign;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.auth.OAuth.OAuthToken;
import com.docusign.esign.client.auth.OAuth.UserInfo;
import com.docusign.esign.model.*;

import java.util.*;
import java.util.List;

public class Docusign {

    private static class AuthResult {
        ApiClient apiClient;
        OAuthToken oAuthToken;
        String accessToken;
        String accountId;
    }

    private static AuthResult createApiClient(Map<String, String> inputMap) throws Exception {
        ApiClient apiClient = new ApiClient("https://demo.docusign.net/restapi");
        apiClient.setOAuthBasePath("account-d.docusign.com");
        ArrayList<String> scopes = new ArrayList<String>();
        scopes.add("signature");
        scopes.add("impersonation");
        byte[] privateKeyBytes = new String("-----BEGIN RSA PRIVATE KEY-----\n" +
                "MIIEowIBAAKCAQEAyhsgXlkGTT4L09/z4u+zkGUFdDZ4zpOA0hb1PRAO8V//75q4\n" +
                "N0yq2REdgvup0uaImt92EYZ6/sEV4IaFamWHTCXNzQ7FcOjNNrg/eDMDY2P4DBMO\n" +
                "CUuf6QF8EYoIcUUDpwn/hntwT/oJOjmWn3+RXpYJ4AR004SqMUuVsmUKjyUgLAJA\n" +
                "AokejE1lnYfwTFwch67u3sASjRo6ExkKfthhSnt5uqlenuvyvTPozi9vqsgnwB8e\n" +
                "I+AaGhyGXcr56OZlRAmOxjCpgqQbqHDUPafcA/LYh0/mT1bfkY2HQ/tgM/kqlppZ\n" +
                "zRbOsQ/gouTu6yLLFzucfQc4ALOP/ICzOpY3EwIDAQABAoIBAAcxbPb+qyM/pJMf\n" +
                "YuI+TsFCXy3SLH2JsWmIyKSqOCXuGUFRW+IX9/Ku0cKSK2qmqtD8SAr6otEDVUB+\n" +
                "+SuevnB+3fLA5lUM7BTY/OVNx36XlcH0QakiCGh2FZDYPVGut6EP0YjbRtWJ3B82\n" +
                "4n39v9JCe8p9zhyjGZ2Uh5joULZkdxZIyOCZXqLWWli0fy9mzUq9RnaPNANVSPNg\n" +
                "iFSjNp31DRTjwvKgWAPiTHKqYcyBIU0Ci5+O1/Cc7AlBPh/KlDisScnBTWMQpuH+\n" +
                "R6jyXfnqV7it4qxAsgWSxP+4e5dN+PxI02EYAQfsvVrCLFrgZRm+Kpoc8ESr7oXL\n" +
                "fCcDbH0CgYEA/T3rdIvFe5i5X6ZOLFVlgOrXcxEWbXXwyegmRy89Kbf0z1bK+rUw\n" +
                "bi9FpXA/S1Y5jlZxq8SrC0h2RrMX7W+JLSYppz/DOHYd0RNZFg8VOfoO2IOqTEYd\n" +
                "0t+gl2VOMILOELi9QBRBfEeyKRz9mwoOmAktVvS0hqBfUwNRUcPp0acCgYEAzE6h\n" +
                "nX1um00HTJXyReyJxBtvXDZndKFdGdQwpB9PWXesQHwIHUyIk57aao2I7DQwA11K\n" +
                "0ZJbJIACCobVlFhcZQh4vYomMiy7oyM0pWha4vtQM7Sq4teg9L1Ax92ix8di7wja\n" +
                "VrLrXmHtkrNywcq3ddMkycE5yredsmJ+IncapLUCgYBKUIFoEhxByO+dQhBusiC3\n" +
                "VDMlcT5DoPioR5f0C8jHFVSiA7ZNHB66NuUZR3dLhGIfgv6YiX4oMXuEA8phwvle\n" +
                "ZnSSwANp7j+RuzvJXWO9P8RbhUfUWj8mQrm9s9zeH4SZI51l0IvnDKMbrYY+mRTv\n" +
                "SMIlYmoXl7hhg/e5JuTWwQKBgG3DyRyd6TDSv5sr7FAY9yM/QiMer5J/p09bKw7i\n" +
                "K3V70DWM7acgGTiGXf5lMC0HFMyZ8LqLkSAZDf+t1U2v82Psm0Hp2P+X/O922LAN\n" +
                "UtckAkDlTfI1uyoCC63Vq8D9pG+cKAHwenfo0QTCGGN5j/tjhx5GC9CXuce/FtlZ\n" +
                "eB0JAoGBAI6aKDvytC+BB3ULbi0e2Nyw/XKwQbqyqzF8yzmDO3ylcaWxGLYOn5La\n" +
                "dkhCnDlD3Qq886vxdVgjZFZb+pDDwbV5v2DJgC4Za+z6HqkLupIOKEpoFEARMDah\n" +
                "kYfSVeCl3Wuuy5RmvJom6oOBeZlUwMWj0C3lsu/6DLRmxAKY4MMz\n" +
                "-----END RSA PRIVATE KEY-----\n").getBytes();

        OAuthToken oAuthToken = apiClient.requestJWTUserToken(
                "091fcd65-70d6-40a3-a581-6e20034eb0d5",
                "567e1f10-18c6-4950-8598-1fd5e7a69388",
                scopes,
                privateKeyBytes,
                3600);

        AuthResult result = new AuthResult();
        result.accessToken = oAuthToken.getAccessToken();
        UserInfo userInfo = apiClient.getUserInfo(result.accessToken);
        result.accountId = userInfo.getAccounts().get(0).getAccountId();
        result.apiClient = apiClient;

        return result;
    }

    public static Envelope getEnvelope(Map<String, String> inputMap) throws Exception {
        // Get access token and accountId
        AuthResult ar = createApiClient(inputMap);

        String envelopeId = inputMap.get("envelopeId");

        ar.apiClient.addDefaultHeader("Authorization", "Bearer " + ar.accessToken);
        EnvelopesApi envelopesApi = new EnvelopesApi(ar.apiClient);
        return envelopesApi.getEnvelope(ar.accountId, envelopeId);
    }

    public static List<EnvelopeDocument> getEnvelopeDocuments(Map<String, String> inputMap) throws Exception {
        // Get access token and accountId
        AuthResult ar = createApiClient(inputMap);

        String envelopeId = inputMap.get("envelopeId");

        ar.apiClient.addDefaultHeader("Authorization", "Bearer " + ar.accessToken);
        EnvelopesApi envelopesApi = new EnvelopesApi(ar.apiClient);
        EnvelopeDocumentsResult edr = envelopesApi.listDocuments(ar.accountId, envelopeId);
        return edr.getEnvelopeDocuments();
    }

    public static String getDocument(Map<String, String> inputMap) throws Exception {
        // Get access token and accountId
        AuthResult ar = createApiClient(inputMap);

        String envelopeId = inputMap.get("envelopeId");
        String documentId = inputMap.get("documentId");

        ar.apiClient.addDefaultHeader("Authorization", "Bearer " + ar.accessToken);
        EnvelopesApi envelopesApi = new EnvelopesApi(ar.apiClient);
        return Base64.getEncoder().encodeToString(envelopesApi.getDocument(ar.accountId, envelopeId, documentId));
    }

    public static EnvelopeSummary createEnvelope(Map<String, String> inputMap) throws Exception {
        // Get access token and accountId
        AuthResult ar = createApiClient(inputMap);

        // Create envelopeDefinition object
        EnvelopeDefinition envelope = new EnvelopeDefinition();
        envelope.setEmailSubject("Please sign this document set");
        envelope.setStatus("sent");

        // Create tabs object
        SignHere signHere = new SignHere();
        signHere.setDocumentId("1");
        signHere.setPageNumber("1");
        signHere.setXPosition("191");
        signHere.setYPosition("148");
        Tabs tabs = new Tabs();
        tabs.setSignHereTabs(Arrays.asList(signHere));
        // Set recipients
        Signer signer = new Signer();
        signer.setEmail("tim.miranda@gmail.com");
        signer.setName("tim miranda");
        signer.recipientId("1");
        signer.setTabs(tabs);
        Recipients recipients = new Recipients();
        recipients.setSigners(List.of(signer));
        envelope.setRecipients(recipients);

        // Add document
        Document document = new Document();
        document.setDocumentBase64("VGhhbmtzIGZvciByZXZpZXdpbmcgdGhpcyEKCldlJ2xsIG1vdmUgZm9yd2FyZCBhcyBzb29uIGFzIHdlIGhlYXIgYmFjay4=");
        document.setName("doc1.txt");
        document.setFileExtension("txt");
        document.setDocumentId("1");
        envelope.setDocuments(List.of(document));

        // Send envelope


        ar.apiClient.addDefaultHeader("Authorization", "Bearer " + ar.accessToken);
        EnvelopesApi envelopesApi = new EnvelopesApi(ar.apiClient);
        return envelopesApi.createEnvelope(ar.accountId, envelope);
    }
}
