package br.meetingplace.application

import br.meetingplace.entities.grupos.Group
import br.meetingplace.entities.usuario.*
import br.meetingplace.servicies.Authentication
import kotlin.random.Random

open class GeneralManagement() {
    //Listas
    protected val groupList = mutableListOf<Group>()
    protected val userList = mutableListOf<Profile>()
    //Listas

    //Log system
    protected var logged = -1
    private val auto = Authentication()

    protected fun logOn(User: Int, Pass: String){
        val indexUser = getIndexUser(User)

        if(indexUser != -1 && userList[indexUser].getPass() == Pass && logged == -1){
            auto.updateLoggedUser(User, -1)
            logged = User
        }

    }
    protected fun logOff(){
        if(logged != -1){
            auto.updateLoggedUser(-1, logged)
            logged = -1
        }
    }
    //Log system


    //Generators
    fun generateIdUser(): Int{
        var id = Random.nextInt(1, 20000)

        for (i in userList){
            while(id == i.getId()) // enquanto novo ID existir ele gera diferentes
                id = Random.nextInt(1, 20000)
        }
        return id
    }

    private fun generateIdGroup(): Int {
        var id = Random.nextInt(1, 20000)

        for (i in groupList) {
            if (i.getId() == id) {
                while (id == i.getId()) {
                    id = Random.nextInt(1, 20000)
                }
            }
        }
        return id
    }
    //Generators


    //Index finders
    fun getIndexGroup(id: Int): Int {
        for(i in 0 until groupList.size) {
            if (groupList[i].getId() == id)
                return i
        }
        return -1
    }

    fun getIndexUser(id: Int): Int {
        for(i in 0 until userList.size) {
            if (userList[i].getId() == id)
                return i
        }
        return -1
    }

    fun getIndexMember(id: Int, idGroup: Int): Int {

        val indexGroup = getIndexGroup(idGroup)

        for(i in 0 until groupList[indexGroup].members.size) {
            if (groupList[indexGroup].members[i].user == id)
                return i
        }
        return -1
    }



    //Index finders

    /*


    fun iniciaChat(Mensagem: Mensagens){
        val chat = ItemMensagem()
        val lista = obterListaUsuario()
        var existe = 0
        val indexDestino = obterIndexUserId(Mensagem.destinatario)
        val indexRemetente = obterIndexUserId(Mensagem.remetente)

        if(indexDestino != -1 && indexRemetente != -1){

            chat.id = (lista[indexRemetente].id?.plus(lista[indexDestino].id!!))
            chat.conversa.add(Mensagem.mensagem + " - " + lista[indexRemetente].obterUserName())

            for(i in 0 until lista[indexRemetente].chat.size){ // Conversa ja existe
                if(lista[indexRemetente].chat[i].id == (lista[indexRemetente].id?.plus(lista[indexDestino].id!!))) {
                    chatCall(Mensagem) // manda para a função de enviar msg ao inves de criar chat
                    existe = 1
                }
            }
            if (existe == 0){

                lista[indexDestino].chat.add(chat)
                lista[indexRemetente].chat.add(chat)
            }
        }
    }

    fun chatCall(Mensagem: Mensagens ) {

        var i = 0
        val lista = obterListaUsuario()
        val indexDestino = obterIndexUserId(Mensagem.destinatario)
        val indexRemetente = obterIndexUserId(Mensagem.remetente)

        if(indexDestino != -1 && indexRemetente != -1){
            while(lista[indexRemetente].chat[i].id != (lista[indexRemetente].id?.plus(lista[indexDestino].id!!))  && i < lista[indexRemetente].chat.size)
                i++
            val index = i
            if (i  == lista[indexRemetente].chat.size && lista[indexRemetente].chat[i].id != lista[indexRemetente].id?.plus(lista[indexDestino].id!!)) // Se a conversa nao existir ele cria uma
                iniciaChat(Mensagem)
            else
                lista[indexRemetente].chat[index].mandaMsg(Mensagem.mensagem, indexRemetente,lista, index)
        }
    }

    fun addAmigo(Friend: Amigos) {
        val indexUser = obterIndexUserId(Friend.atual)
        val indexFriend = obterIndexUserId(Friend.externo)

        if (indexUser != -1 && indexFriend != -1) { //Se os dois nomes passados existem
            if (listaUsuarios[indexFriend].obterUserName() !in listaUsuarios[indexUser].amigos) // se nao for amigo ent add
                listaUsuarios[indexUser].amigos.add(listaUsuarios[indexFriend].obterUserName())
        }
    }

    fun removeAmigo(Friend: Amigos){
        val indexUser = obterIndexUserId(Friend.atual)
        val indexFriend = obterIndexUserId(Friend.externo)
        if (indexUser != -1 && indexFriend != -1) { //Se os dois nomes passados existem
            if (listaUsuarios[indexFriend].obterUserName() in listaUsuarios[indexUser].amigos) // se for amigo ent remove
                listaUsuarios[indexUser].amigos.remove(listaUsuarios[indexFriend].obterUserName())
        }
    }



    fun logarNaConta(novoLogin: Login): String{
        for(usuario in listaUsuarios){
            if(usuario.obterUserName() == novoLogin.obterUserName()){
                if(usuario.obterSenha() == novoLogin.obterSenha()){
                    usuarioLogado = usuario
                    login.logar(usuario.obterUserName(), usuario.obterSenha())
                    return "Login efetuado com sucesso"
                }
                else{
                    return "Senha incorreta"
                }
            }
        }

        return "Usuario não cadastrado"
    }


    //GRUPOS

    fun criarGrupo(grupo: Grupo): String{
        return if(login.obterStatus()){
            if(verificador.nomeEmListaGrupo(grupo.obterNome(), listaGrupos)){
                "Nome do grupo já utilizado"
            }
            else{
                grupo.inicializarGrupo(usuarioLogado)
                usuarioLogado.addGrupo(grupo.obterNome())
                listaGrupos.add(grupo)
                "Grupo ${grupo.obterNome()} criado com sucesso"
            }
        }
        else{
            "Nínguem está logado no momento"
        }
    }

    fun apagarGrupo(grupo: Grupo): String {

        return if (login.obterStatus() == false) {
            "Nínguem está logado no momento"
        } else {
            if (verificador.nomeEmListaGrupo(grupo.obterNome(), listaGrupos)) {
                val index = obterIndexGrupo(grupo.obterNome())

                for (usuario in listaUsuarios) {
                    usuario.removeGrupo(grupo.obterNome())
                }
                listaGrupos.removeAt(index)
                "Grupo removido com sucesso"
            } else {
                "Grupo não exite ou nome errado"
            }
        }
    }




    fun mensagemGrupo(Mensagem: GrupoMensagens){

        val indexUsuario = obterIndexUserId(Mensagem.remetente)
        val indexGrupo = obterIndexGrupo(Mensagem.grupo)

        if(indexUsuario != -1 && indexGrupo != -1){ // se o usuario existir e o grupo
            if(listaUser[indexUsuario].obterUserName() in listaGrupos[indexGrupo].membros || listaUser[indexUsuario].obterUserName()  in listaGrupos[indexGrupo].membrosAdmin ) // se o usuario for membro do grupo
                listaGrupos[indexGrupo].obterMensagens().add("${Mensagem.mensagem} <- ${listaUser[indexUsuario].obterUserName()}")
        }
    }

    fun addMembroGrupo(Membro: Membro){

        val indexUsuario = obterIndexUserId(Membro.usuario)
        val indexGrupo = obterIndexGrupo(Membro.grupo)

        if(indexUsuario != null && indexGrupo != null){
            if(listaUser[indexUsuario].obterUserName() !in listaGrupos[indexGrupo].membros &&
                listaUser[indexUsuario].obterUserName() !in listaGrupos[indexGrupo].membrosAdmin ){
                // se ele nao estiver na lista de membros e de adminsentao adiciona
                listaGrupos[indexGrupo].membros.add(listaUser[indexUsuario].obterUserName() )
                listaUser[indexUsuario].grupos.add(listaGrupos[indexGrupo].id!!)
            }
        }
    }

    fun addAdminGrupo(Membro: Membro){

        val indexAdd = obterIndexUserId(Membro.usuario)
        val indexGrupo = obterIndexGrupo(Membro.grupo)

        if(indexAdd != null &&  indexGrupo != null){ // se os membros e o grupo existirem
            if(listaUser[indexAdd].obterUserName() !in listaGrupos[indexGrupo].membrosAdmin && listaUser[indexAdd].obterUserName()  in listaGrupos[indexGrupo].membros){ // se nao for admin mas for membro normal
                listaGrupos[indexGrupo].membrosAdmin.add(listaUser[indexAdd].obterUserName() )
                removeMembroGrupo(Membro)
            }
            else if(listaUser[indexAdd].obterUserName()  !in listaGrupos[indexGrupo].membrosAdmin && listaUser[indexAdd].obterUserName()  !in listaGrupos[indexGrupo].membros){// se nao estiver no grupo
                listaUser[indexAdd].grupos.add(listaGrupos[indexGrupo].id!!)
                listaGrupos[indexGrupo].membrosAdmin.add(listaUser[indexAdd].obterUserName() )
            }
        }
    }

    fun removeMembroGrupo(Membro: Membro){

        val indexRemovido =obterIndexUserId(Membro.usuario)
        val indexGrupo = obterIndexGrupo(Membro.grupo)

        if(indexGrupo != -1 &&  indexRemovido!= -1){

            listaGrupos[indexGrupo].membros.remove(listaUser[indexRemovido].obterUserName() )
            listaUser[indexRemovido].grupos.remove(listaGrupos[indexGrupo].id)
        if(indexGrupo != null &&  indexRemovido!= null){
            if(listaUser[indexRemovido].obterUserName() in listaGrupos[indexGrupo].membros ||listaUser[indexRemovido].obterUserName() in listaGrupos[indexGrupo].membrosAdmin){
                listaGrupos[indexGrupo].membros.remove(listaUser[indexRemovido].obterUserName())

                listaGrupos[indexGrupo].membrosAdmin.remove(listaUser[indexRemovido].obterUserName())
                listaUser[indexRemovido].grupos.remove(listaGrupos[indexGrupo].id!!)
            }
        }
    }


    */
}
