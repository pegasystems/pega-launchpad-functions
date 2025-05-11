package com.pega.launchpad.docusign;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.client.auth.OAuth.OAuthToken;
import com.docusign.esign.client.auth.OAuth.UserInfo;
import com.docusign.esign.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class Docusign {

    /**
     * Given an envelope ID, return the Envelope json object
     *
     * @param inputMap Must contain authentication info, and envelopeId
     * @return Object an com.docusign.esign.model.Envelope object
     * @throws IOException,ApiException if error
     */
    public static Object getEnvelope(Map<String, String> inputMap) throws IOException, ApiException {
        // Get access token and accountId
        AuthResult ar = createApiClient(inputMap);

        String envelopeId = inputMap.get("envelopeId");


        EnvelopesApi envelopesApi = new EnvelopesApi(ar.apiClient);
        return envelopesApi.getEnvelope(ar.accountId, envelopeId);
    }

    /**
     * @param inputMap Must contain authentication info, and envelopeId
     * @return Object com.docusign.esign.model.EnvelopeDocumentsResult object
     * @throws IOException  if error
     * @throws ApiException if error
     */
    public static Object getEnvelopeDocuments(Map<String, String> inputMap) throws IOException, ApiException {
        // Get access token and accountId
        AuthResult ar = createApiClient(inputMap);

        String envelopeId = inputMap.get("envelopeId");


        EnvelopesApi envelopesApi = new EnvelopesApi(ar.apiClient);
        return envelopesApi.listDocuments(ar.accountId, envelopeId);
    }

    /**
     * @param inputMap Must contain authentication info, envelopeId, and documentId
     * @return String base64 encoded document content
     * @throws IOException  if error
     * @throws ApiException if error
     */
    public static String getDocument(Map<String, String> inputMap) throws IOException, ApiException {
        // Get access token and accountId
        AuthResult ar = createApiClient(inputMap);

        String envelopeId = inputMap.get("envelopeId");
        String documentId = inputMap.get("documentId");


        EnvelopesApi envelopesApi = new EnvelopesApi(ar.apiClient);
        return Base64.getEncoder().encodeToString(envelopesApi.getDocument(ar.accountId, envelopeId, documentId));
    }

    /**
     * @param inputMap Must contain authentication info, subject, status, signerEmail, signerNAme, documentContent, documentName, documentExtension
     * @return Object a com.docusign.esign.model.EnvelopeSummary object
     * @throws IOException  if error
     * @throws ApiException if error
     */
    public static Object createEnvelope(Map<String, String> inputMap) throws IOException, ApiException {
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
        tabs.setSignHereTabs(List.of(signHere));
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

        EnvelopesApi envelopesApi = new EnvelopesApi(ar.apiClient);
        return envelopesApi.createEnvelope(ar.accountId, envelope);

    }

    private static AuthResult createApiClient(Map<String, String> inputMap) throws IOException, ApiException {
        ApiClient apiClient = new ApiClient(inputMap.getOrDefault("basePath", "https://demo.docusign.net/restapi"));
        apiClient.setOAuthBasePath(inputMap.getOrDefault("oAuthBasePath", "account-d.docusign.com"));
        ArrayList<String> scopes = new ArrayList<>();
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


        apiClient.addDefaultHeader("Authorization", "Bearer " + result.accessToken);

        return result;
    }

    private static class AuthResult {
        ApiClient apiClient;
        String accessToken;
        String accountId;
    }


}
