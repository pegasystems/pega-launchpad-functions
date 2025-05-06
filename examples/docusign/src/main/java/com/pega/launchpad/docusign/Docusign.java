package com.pega.launchpad.docusign;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.auth.OAuth.OAuthToken;
import com.docusign.esign.client.auth.OAuth.UserInfo;
import com.docusign.esign.model.*;
import com.google.gson.Gson;

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
        ApiClient apiClient = new ApiClient(inputMap.getOrDefault("basePath","https://demo.docusign.net/restapi"));
        apiClient.setOAuthBasePath(inputMap.getOrDefault("oAuthBasePath", "account-d.docusign.com"));
        ArrayList<String> scopes = new ArrayList<String>();
        scopes.add("signature");
        scopes.add("impersonation");
        byte[] privateKeyBytes = Base64.getDecoder().decode(inputMap.get("privateKeyBase64"));
        OAuthToken oAuthToken = apiClient.requestJWTUserToken(
                inputMap.get("clientId"),
                inputMap.get("userId"),
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

    public static Object createEnvelope(Map<String, String> inputMap) throws Exception {
        // Get access token and accountId
        AuthResult ar = createApiClient(inputMap);

        // Create envelopeDefinition object
        EnvelopeDefinition envelope = new EnvelopeDefinition();
        envelope.setEmailSubject(inputMap.getOrDefault("subject", "test subject"));
        envelope.setStatus(inputMap.getOrDefault("status", "sent"));

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
        signer.setEmail(inputMap.get("signerEmail"));
        signer.setName(inputMap.get("signerName"));
        signer.recipientId("1");
        signer.setTabs(tabs);
        Recipients recipients = new Recipients();
        recipients.setSigners(List.of(signer));
        envelope.setRecipients(recipients);

        // Add document
        Document document = new Document();
        document.setDocumentBase64(inputMap.get("documentContent"));
        document.setName(inputMap.get("documentName"));
        document.setFileExtension(inputMap.get("documentExtension"));
        document.setDocumentId("1");
        envelope.setDocuments(List.of(document));

        // Send envelope
        ar.apiClient.addDefaultHeader("Authorization", "Bearer " + ar.accessToken);
        EnvelopesApi envelopesApi = new EnvelopesApi(ar.apiClient);
        EnvelopeSummary es = envelopesApi.createEnvelope(ar.accountId, envelope);
        String json = new Gson().toJson(es);
        return new Gson().fromJson(json, EnvelopeSummary.class);
    }
}
