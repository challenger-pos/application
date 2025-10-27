package com.fiap.core.exception.enums;

public enum ErrorCodeEnum {

    CAD0001("Documento Inválido", "CAD-0001"),
    CAD0002("Documento já cadastrado!", "CAD-0002"),
    CAD0003("Email já cadastrado!", "CAD-0003"),
    CAD0004("Erro na criação do cliente", "CAD-0004"),

    USE0001("A senha não pode ser nula", "USE-0001"),
    USE0002("A senha deve ter pelo menos 8 caracteres", "USE-00022"),
    USE0003("A senha deve conter pelo menos uma letra maiúscula", "USE-0003"),
    USE0004("A senha deve conter pelo menos uma letra minúscula", "USE-0004"),
    USE0005("A senha deve conter pelo menos um número", "USE-0005"),
    USE0006("A senha deve conter pelo menos um caractere especial (!@#$%^&*()-+)", "USE-0006"),
    USE0007("Usuário não encontrado", "USE-0007"),
    USE0008("Erro na criação do usuário", "USE-0008"),
    USE0009("O mechanicId deve ser de um mecânico cadastrado no sistema", "USE-0009"),

    CUST0001("Cliente não encontrado", "CUST-0001"),

    VEH0001("Veículo não encontrado", "VEH-0001"),

    PART0001("Peça não encontrada", "PAR-0001"),
    PART0002("Estoque de peças insuficiente", "PAR-0002"),

    SERV0001("Serviço não encontrado", "SERV-0001"),

    WORK0001("Ordem de serviço não encontrada", "WORK-0001"),
    WORK0002("É necessário inserir pelo menos uma peça ou serviço para a ordem de serviço.", "WORK-0002"),
    WORK0003("Ordem de serviço encontra-se indisponível para definição de mecânico", "WORK-0003"),
    WORK0004("Status informado é inválido ou não permitido para a ordem de serviço.", "WORK-0004"),
    WORK0005("Status informado já foi atribuído para a ordem de serviço.", "WORK-0005"),
    WORK0006("Ordem de serviço não se encontra pendente de aprovação", "WORK-0006"),

    HIST0001("Histórico de ordens de serviço não encontrado para o CPF/CNPJ informado.", "HIST-0001");

    private String message;
    private String code;

    ErrorCodeEnum(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
