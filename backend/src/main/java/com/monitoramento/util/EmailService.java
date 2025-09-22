package com.monitoramento.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Value("${spring.mail.username}")
    String from;

    @Autowired
    private JavaMailSender mailSender;


    public void sendPasswordResetEmail(String to, String resetUrl) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String html = """
            <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
                "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
            <html xmlns="http://www.w3.org/1999/xhtml">
            <head>
              <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
              <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
              <meta http-equiv="X-UA-Compatible" content="IE=Edge">
              <link href="https://fonts.googleapis.com/css?family=Open+Sans:400,600,700&display=swap" rel="stylesheet">
              <style type="text/css">
                body, p, div { font-family: 'Open Sans', sans-serif; font-size: 15px; line-height: 1.6; }
                body { color: #333333; margin: 0; padding: 0; background-color: #f4f4f4; }
                body a { color: #007bff; text-decoration: none; }
                p { margin: 0; padding: 0; }
                table.wrapper { width:100%% !important; table-layout: fixed; -webkit-font-smoothing: antialiased; }
                img.max-width { max-width: 100%% !important; height: auto; display: block; }
                .button {
                  background-color: #D32F2F; /* Vermelho que remete ao logo START */
                  color: #ffffff;
                  padding: 12px 25px;
                  text-decoration: none;
                  font-size: 16px;
                  font-weight: 600;
                  display: inline-block;
                  border-radius: 8px;
                  text-align: center;
                  border: 1px solid #C62828;
                }
                .container { max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 8px rgba(0,0,0,0.05); }
                .header { background-color: #FFFFFF; padding: 20px 30px; text-align: center; }
                .content { padding: 30px; }
                .footer { background-color: #f9f9f9; padding: 20px 30px; text-align: center; font-size: 12px; color: #777777; border-top: 1px solid #eeeeee; }
                .ii a[href] {color: white;}
                @media screen and (max-width:480px) {
                  .container { margin: 10px; border-radius: 0; }
                  .content, .header, .footer { padding: 20px; }
                  .button { padding: 10px 20px; font-size: 14px; width: 80%%; }
                }
              </style>
            </head>
            <body>
              <center class="wrapper">
                <div class="webkit">
                  <table cellpadding="0" cellspacing="0" border="0" width="100%%" bgcolor="#f4f4f4">
                    <tr>
                      <td valign="top" width="100%%">
                        <table class="container" cellpadding="0" cellspacing="0" border="0" align="center">
                          <tr>
                            <td class="header">
                              <img class="max-width"
                                   src="cid:startLogo"
                                   alt="START Colégio e Curso" width="200" style="display:block;margin:0 auto;">
                            </td>
                          </tr>
                          <tr>
                            <td class="content">
                              <h1 style="font-size:22px; color:#333333; margin-top:0; margin-bottom: 20px; text-align: center;">Redefinição de Senha</h1>
                              <p style="margin-bottom: 15px;">Recebemos uma solicitação para redefinir a senha da sua conta no <strong>START Colégio e Curso</strong>.</p>
                              <p style="margin-bottom: 25px;">Para criar uma nova senha, por favor, clique no botão abaixo:</p>
                              <p style="text-align:center; margin-top:20px; margin-bottom:30px;">
                                <a href="%s" class="button">
                                  Redefinir Minha Senha
                                </a>
                              </p>
                              <p style="font-size:14px; margin-bottom: 15px;">Se você não solicitou a redefinição de senha, por favor, ignore este e-mail. Sua senha atual permanecerá inalterada.</p>
                              <p style="font-size:14px;">Atenciosamente,<br><strong>Equipe START Colégio e Curso</strong></p>
                            </td>
                          </tr>
                          <tr>
                            <td class="footer">
                              <p>&copy; %d START Colégio e Curso. Todos os direitos reservados.</p>
                              <p>Este é um e-mail automático. Por favor, não responda.</p>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </div>
              </center>
            </body>
            </html>
            """.formatted(resetUrl, java.time.Year.now().getValue());

        helper.setTo(to);
        helper.setFrom(from);
        helper.setSubject("Redefinição de Senha - START Colégio e Curso");
        helper.setText(html, true);

        // Adicionando a imagem como um recurso embutido (inline)
        // O nome 'startLogo' no HTML (src="cid:startLogo") deve corresponder a este ID
        ClassPathResource logo = new ClassPathResource("static/images/logo_start.webp"); // <-- Caminho da sua imagem
        helper.addInline("startLogo", logo);

        mailSender.send(message);
    }


}
