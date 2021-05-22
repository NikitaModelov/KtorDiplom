package ru.modelov.graduatestudents.contoller.email

import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.SimpleEmail

class EmailController {

    fun sendEmail(nameUser: String, emailUser: String, passwordUser: String) {
        val emailLogin = "modelov.diplom@gmail.com"
        val password = "cffspaezobjvhaqp"

        val email = SimpleEmail()
        email.hostName = "smtp.googlemail.com"
        email.setSmtpPort(465)
        email.setAuthenticator(DefaultAuthenticator(emailLogin, password))
        email.isSSLOnConnect = true
        email.setFrom(emailLogin)
        email.subject = "Доступ к приложению \"Выпускник СГУПСа\""
        email.setMsg("Добро пожаловать, $nameUser!\nВаш логин: $emailUser\nВаш пароль: $passwordUser")
        email.addTo(emailUser)
        email.send()
    }
}