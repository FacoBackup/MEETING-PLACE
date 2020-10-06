package br.meetingplace

import br.meetingplace.application.*
import br.meetingplace.entities.grupos.*
import br.meetingplace.entities.usuario.*

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

val SystemV = GeneralManagement()

fun main (){

    /*
    embeddedServer(Netty, 8823) {
        routing {
            install(ContentNegotiation) {
                gson {
                    setPrettyPrinting()
                }
            }

            // Relacionado a usuarios
            get("/usuario/mostrar_usuarios"){
                call.respond(sysManagement.obterListaUsuario())
            }
            post("/usuario/cadastrar"){
                var novoUsuario = call.receive<Perfil>()
                call.respond(sysManagement.criarUsuario(novoUsuario))
            }
            patch("/login"){
                var login = call.receive<Login>()
                call.respond(sysManagement.logarNaConta(login))
            }
            get("/login/usuario_logado"){
                call.respond(
                        if(sysManagement.obterLogin().obterStatus())
                            sysManagement.obterUsuarioLogado().toString()
                        else
                            "Ninguém está logado"
                )
            }
            get("/login/grupos_do_usuario"){
                call.respond(sysManagement.obterUsuarioLogado().grupos)
            }
            post("/login/criar_grupo"){
                var novoGrupo = call.receive<Grupo>()
                call.respond(sysManagement.criarGrupo(novoGrupo))
            }
            get("/grupo/mostrar_grupos"){
                call.respond(sysManagement.obterListaGrupos().toString())
            }
            delete("/grupos/apagar_grupo"){
                var grupo = call.receive<Grupo>()
                call.respond(sysManagement.apagarGrupo(grupo))
            }

        }
    }.start(wait = true)

     */
}


