package modelo;

public class EstoqueInsuficienteException extends Exception{

    public EstoqueInsuficienteException(String mensagem){
        super (mensagem);
    }
}
