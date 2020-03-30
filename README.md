# 1. Descrierea problemei

Se dorește implementarea unui sistem de management financiar participativ prin intermediul căruia mai mulți utilizatori pot ține evidența cheltuielilor comune.

Un utilizator va putea crea un plan de buget pe o anumită zi, fixând o sumă limită ce poate fi cheltuită. Adăugând la rândul alți participanți la planul creat, aceștia vor putea putea isnera noi chestuieli sau le vor putea edita pe cele deja existente în funcție de drepturile deținute.

Se vor implementa de asemenea și strategii de raportare pe diferite canale de comunicare(pentru început email) pentru diferite alerte(de exemplu depășirea pragului de cheltuieli a unui nou buget, adăugarea unui utilizator în plan), un sistem de audit pentru menținerea și stocarea tuturor acțiunilor făcute în cadrul unui plan, funcții de statistici, etc.

# 2. Soluția propusă

Pentru a rezolva problema sus-enunțată se va proiecta și implementa o aplicație web care se va folosi de un API 'remote' care va servi datele necesare prezentării și generării interfeței.

## 2.1. Descriere soluție propusă

Orice utilizator înregistrat în aplicație va putea să creeze și să gestioneze bugete.

În cadrul unui buget pot fi setate o limită maximă de cheltuieli și data pe care aceasta validă. Alți utilizatori pot fi adăugați pe urmă să ia parte în planificarea acestuia prin intermediul unor roluri. 

### 2.1.1. Rolurile din cadrul unui buget

Următoarele roluri sunt ierarhice, adică un rol de pe poziția n are aceleași drepturile cu rolurile de poziția n - 1, n - 2, ... și altele în plus.

1. _Viewer_, poate vedea detaliile bugetului
2. _Editor_, poate edita și adăuga cheltuieli
3. _Administrator_, poate adăuga noi roluri, să le schimbe pe cele deja existente(dar numai roluri ierarhic mai joc) și schimba limita de cheltuire
4. _Creator_, poate adăuga noi administatori

## 2.2. Backend-ul soluției

Acesta are scopul de a deservi consumatoriul de date(în acest caz aplicația frontend), a asigura persistarea datelor și efectuarea de calcule asupra acestora.

Pentru implementarea părții de backend a proiectului se va folosi SpringBoot. S-a ales folosirea acestuia pentru a ușura maparea dependențelor între componentele aplicației și pentru capabilitățile existene în cadrul librăriei care facilitează crearea de api-uri.

Este necesară specificarea că pentru fiecare parte a logicii de aplicație(pentru operațiile de creare și alterare buget, adăugare chetuieli, etc.) s-a creat câte un controller care expune endpoint-uri pentru fiecare operație în parte.

### 2.2.1. Endpoint-uri API

Următoarele endpoint-uri sunt puse la dispoziție pentru consumatorii de date:

###### POST */auth/signup*

- permite crearea unuit utilizator în cadrul aplicației
- returnează utilizatorul creat dacă acesta este valid

Exemplu payload:

```JSON
{
	"email": "user@email.com",
	"password": "123abc"
}
```
Exemplu răspuns returnat pentru un scenariu de succes:

```JSON
{
    "data": null,
    "messages": [
        "User created"
    ],
    "errorMessages": []
}
```

###### POST */auth/login*

- permite autentificarea unui utilizator existen în aplicație
- returnează token-ul creat aferent autentificării

Exemplu payload:

```JSON
{
	"email": "user@email.com",
	"password": "123abc"
}
```
Exemplu răspuns returnat pentru un scenariu de succes:

```JSON
{
    "data": {
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhdXRoZW50aWNhdGlvbiIsImVtYWlsIjoidXNlckBlbWFpbC5jb20iLCJleHAiOjE1ODU1MDQzNjR9.zTDpfmwfTjN0wTWg4CzU6TWLYKV1c06lIyUizbADYcQ",
        "userEmail": "user@email.com"
    },
    "messages": [
        "Logged in"
    ],
    "errorMessages": []
}
```

###### POST */auth/logout*

- permite delogarea unui utilizator
- token-ul utilziatorului va fi șters din baza de date

Exemplu răspuns returnat pentru un scenariu de succes:

```JSON
{
    "data": null,
    "messages": [],
    "errorMessages": [
        "User logged out"
    ]
}
```

###### POST */api/budget*

- permite crearea unui nou buget zilnic folosind parametri primiți
- crează un nou rol în cadrul bugetului automat

Următoarele headere trebuie setate pentru a putea face request-ul:

| Nume  | Valoare |
| ------ | ------ |
| Authorization | Token-ul de autentificare a autilizatorului logat|

Exemplu payload:

```JSON
{
	"name": "Test budget 1",
	"comment": "first tryout",
	"imposedLimit": 2000,
	"currencyCode": "RON"
}
```

Exemplu răspuns returnat pentru un scenariu de succes:

```JSON
{
    "data": {
        "id": "356de700-a015-4f89-b34f-5cbef496b3c7",
        "date": "2020-03-22T19:38:43.432+0000",
        "name": "Test budget 1",
        "comment": "first tryout",
        "imposedLimit": 2000,
        "currencyCode": "RON",
        "expenditureList": null,
        "budgetRoleList": [
            {
                "id": "76110d7e-26f8-43cb-b8ec-5e2cc65ae305",
                "creationTime": "2020-03-22T19:38:43.638+0000",
                "enabled": true,
                "roleType": "CREATOR",
                "user": {
                    "id": "592a4d5b-088a-4c3b-8467-e65e08a8af8e",
                    "firstName": null,
                    "lastName": null,
                    "email": "user@email.com",
                    "roleList": null
                },
                "creator": null,
                "createdRoles": null,
                "dailyBudgetList": null
            }]
    },
    "messages": [
        "Daily budget and creator role created."
    ],
    "errorMessages": []
}
```

###### POST */api/role?budget=budget_id*

- permite crearea unui nou rol în cadrul unui buget existent pentru un utilizator al aplicației care nu deține deja un rol

Următoarele headere trebuie setate pentru a putea face request-ul:

| Nume  | Valoare |
| ------ | ------ |
| Authorization | Token-ul de autentificare a autilizatorului logat|

Exemplu payload:

```JSON
{
	"user": {
		"id": "23237f3a-ec47-4ef2-a866-82b0289aff5e"
	},
	"roleType": "EDIT"

```

Exemplu răspuns returnat pentru un scenariu de succes:

```JSON
{
    "data": {
        "id": "5f7a7285-aefa-4045-8050-e633156fa46b",
        "creationTime": "2020-03-30T17:02:04.407+0000",
        "enabled": true,
        "roleType": "EDIT",
        "user": {
            "id": "23237f3a-ec47-4ef2-a866-82b0289aff5e",
            "firstName": null,
            "lastName": null,
            "email": null,
            "roleList": null
        },
        "creator": null,
        "createdRoles": null,
        "dailyBudget": null
    },
    "messages": [
        "Role created successfuly"
    ],
    "errorMessages": []
}
```

###### POST */api/expenditurebudget=budget_id*

- permite adăugarea unei cheltuieli în cadrul unui buget existent

Următoarele headere trebuie setate pentru a putea face request-ul:

| Nume  | Valoare |
| ------ | ------ |
| Authorization | Token-ul de autentificare a autilizatorului logat|

Exemplu payload:

```JSON
{
	"amount": 501,
	"comment": "501 ron for you",
	"type": "LARGE"
}
```

Exemplu răspuns returnat pentru un scenariu de succes:

```JSON
{
    "data": {
        "id": "5aadde13-f25e-4fb5-a9b1-310b1763bec9",
        "creationTime": "2020-03-30T17:05:00.240+0000",
        "amount": 501,
        "comment": "1060 ron for you",
        "type": "LARGE",
        "dailyBudget": null
    },
    "messages": [
        "Expenditure created successfuly"
    ],
    "errorMessages": []
}
```

## 3. Implementare

### 3.1. Proiectarea bazei de date

S-a ales folosirea unei baze de date MySQL. Pentru conectarea la acesta s-a ales folosirea ORM-ului Hibernate. În acest mod pentru partea de modelare câmpurile entităților sunt adnotate specifi în funcției de maparea la o coloană corespondentă din tabelele bazei de date. Pe partea de tranzacționare și mapare a query-rilor se vor folosi componenete SpringBoot, și anume JPA Repositories.

### 3.1.1. Enități folosite

- _Alert_, stochează alertele aferente unui buget(e.x. alertă de depășire limită cheltuieli)
- _AuthJWT_, salvează tokenele JWT folosite pentru autentificare utilizatorilor în aplicație
- _BudgetRole_, salvează rolurile create în cadrul bugetelor din aplicație
- _Expenditure_, cuprinde detalii despre fiecare cheltuială efectuată(e.x. momentul de timp la care a fost introdusă, totalul cheltuit, utilizatorul care a introdus-o, bugetul de care aparține)
- _User_, a cărei entry-uri sunt identifcare unic de către email și UUID
- _DailyBudget_, conține detalii referitoare la fiecare buget creat în aplicație și anume ziua pentru care este creat și totatul care poate fi cheltuit
### 3.1.2. Diagrama bazei de date

![db diagram](https://github.com/MarcusGitAccount/Kor/blob/master/src/main/resources/images/bd_diagram.PNG)

## 4. TODO

- move logic from controllers to particular logic classes and use those in controllers. This way we can remove the framework dependency in unit tests.
- finish implementing the application required endpoints
- implement the observer pattern for email notifications when the remaining budget is under a certain limit
- add validation methods
- add authentication filters
- improve on the doc(class diagrams, actors presentation, implementation details, etc...)
