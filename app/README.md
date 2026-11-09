# Nivelamento Digital

## Sobre o projeto

O **Nivelamento Digital** é um aplicativo Android desenvolvido como Trabalho Final da Unidade Curricular de Desenvolvimento para Dispositivos Móveis do IFSC.

O objetivo do aplicativo é utilizar os sensores do celular para identificar inclinações, funcionando de maneira semelhante a um nível de bolha utilizado na construção civil.

O aplicativo permite visualizar a inclinação do aparelho, realizar medições, registrar a localização e consultar medições anteriores.

## Funcionalidades

- **Nível horizontal:** apresenta visualmente a inclinação horizontal do aparelho.
- **Nível vertical:** apresenta visualmente a inclinação vertical.
- **Nível circular:** permite visualizar a inclinação em diferentes direções.
- **Calibração:** utiliza a posição atual do celular como referência.
- **Geolocalização:** obtém informações de latitude e longitude.
- **Salvamento de medições:** armazena os resultados no banco de dados SQLite.
- **Histórico:** permite consultar as medições salvas anteriormente.

## Tecnologias utilizadas

- Java: linguagem utilizada no desenvolvimento.
- Android Studio: ambiente de desenvolvimento.
- XML: estruturação das telas.
- Acelerômetro: identificação da inclinação do aparelho.
- GPS: obtenção da localização geográfica.
- SQLite: armazenamento e recuperação das medições.
- Git e GitHub: controle de versão e compartilhamento do código.

## Banco de dados

O aplicativo utiliza SQLite para armazenar as medições diretamente no dispositivo.

São registrados dados como:

- Ângulos X e Y.
- Desnivelamento.
- Situação do nivelamento.
- Data e horário.
- Latitude e longitude, quando disponíveis.
- Precisão do GPS.
- Observações.

O histórico permite consultar posteriormente os registros armazenados.

## Como executar o projeto

1. Clone ou baixe este repositório.
2. Abra o projeto no Android Studio.
3. Aguarde a sincronização do Gradle.
4. Execute o aplicativo em um dispositivo Android ou emulador compatível.
5. Conceda as permissões de localização quando solicitadas.

Para testar o funcionamento do acelerômetro e do GPS, recomenda-se utilizar um celular físico.

## Integrantes

- Antônio Airton
- Isabelle Kohler 
- Lucas Valle

## Instituição

Instituto Federal de Santa Catarina (IFSC)  
Câmpus Gaspar

**Unidade Curricular:** Desenvolvimento para Dispositivos Móveis  
**Trabalho Final — 2026**
