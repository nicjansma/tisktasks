package com.nicjansma.tisktasks;

/**
 * Preferences interface.
 */
public interface IPreferences
{
    /**
     * Gets the user's email.
     *
     * @return The user's email
     */
    String email();

    /**
     * Sets the user's email.
     *
     * @param email User's email
     */
    void setEmail(String email);

    /**
     * Gets the user's API token.
     *
     * @return The user's API Key
     */
    String apiToken();

    /**
     * Sets the user's API token.
     *
     * @param apiKey User's API token
     */
    void setApiToken(String apiKey);

    /**
     * Determine if the user wants to be kept logged in.
     *
     * @return True if the user wants to be keep logged in
     */
    boolean keepLoggedIn();

    /**
     * Sets whether the user wants to be kept logged in.
     *
     * @param keepLoggedIn True to keep the user logged in
     */
    void setKeepLoggedIn(boolean keepLoggedIn);

    /**
     * Determines if Analytics are enabled for the user.
     *
     * @return True if analytics are enabled
     */
    boolean analyticsEnabled();
}
