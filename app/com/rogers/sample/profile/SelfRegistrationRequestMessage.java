package com.rogers.sample.profile;

import play.data.validation.Constraints;

public class SelfRegistrationRequestMessage {

    /*@Constraints.Required
    private String lang;*/
    @Constraints.Email
    @Constraints.Required
    private String email;
    /*@Constraints.Required
    private String question;
    @Constraints.Required
    private String answer;
    @Constraints.Required
    private String password;*/

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /*public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLang(){
        return lang;
    }

    public void setLang(String lang){
        this.lang = lang;
    }
*/


}
