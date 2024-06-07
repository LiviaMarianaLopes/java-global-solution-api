# API VitaliSea 🌊

## Descrição
A API da Vitalisea foi desenvolvida para apoiar a missão da organização de proteger os oceanos através da facilitação de práticas sustentáveis e da preservação do meio ambiente subaquático. A API permite a gestão de voluntários, parceiros, eventos e alertas, proporcionando uma interface backend robusta para um sistema de gerenciamento completo. Além disso, a API integra-se com serviços externos para validação de CEPs e e-mails, garantindo a qualidade dos dados inseridos no sistema.

## Sobre a Vitalisea
A VitaliSea é uma start-up que busca auxiliar nas práticas sustentáveis de preservação do meio ambiente, principalmente subaquáticas. Nosso principal projeto é o SeaKer, uma IA para reconhecer a qualidade da água. Além disso, temos um sistema de alertas para que nossos colaboradores possam enviar notificações de locais que necessitam da nossa ajuda.

## Funcionalidades
- **CRUD para Voluntários e Parceiros**: Cadastro, leitura, atualização e exclusão de contas de voluntários e parceiros.
- **Gerenciamento de Eventos**: Criação, leitura, atualização e exclusão de eventos, incluindo a inscrição de voluntários.
- **Envio de Alertas**: Envio de alertas que incluem informações detalhadas sobre a localização e a descrição do ocorrido.
- **Integração com APIs Externas**:
  - **ViaCEP**: Validação de CEPs nos alertas.
  - **Abstract**: Validação de e-mails de voluntários e parceiros.

## Estrutura do Projeto
- **Entidades Principais**:
  - **Volunteer e Partner**: Extendem a entidade Collaborator.
  - **Event**: Extende a entidade Alert.
  - **Alert**: Relaciona-se com Collaborators através do ID do colaborador que criou o alerta.
    - Cada alerta inclui informações sobre o local e a descrição do ocorrido.
- **Entidades Auxiliares**:
  - **Collaborator**: Base para Voluntários e Parceiros.
  - **Location**: Utilizada em Alertas.
  - **eventsVolunteers**: Tabela intermediária para o relacionamento entre Eventos e Voluntários.
  - **_BaseEntity**: Base para todas as entidades.

## Tecnologias Utilizadas
- **Java**: Linguagem de programação principal.
- **SQL**: Sistema de gerenciamento de banco de dados.
- **Maven**: Gerenciador de dependências.
- **GitHub**: Plataforma para versionamento de código.

## Instalação e Execução
### Pré-requisitos
- Java JDK 11 ou superior
- Maven

### Passos para Executar
1. Clone o repositório:
   ```bash
   git clone https://github.com/LiviaMarianaLopes/java-global-solution-api.git
2. Navegue até o diretório do projeto:
   ```bash
      cd java-global-solution-api
3. Compile e rode a classe principal Main:
   ```bash
      javac -d target src/main/java/org.example/Main.java
      java  -cp target main.java.org.example.Main
