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

###### GET */api/expenditurebudget=budget_id&type=report=report_type*

- permite generarea unui raport de tip furnizat ca și parametru

Următoarele headere trebuie setate pentru a putea face request-ul:

| Nume  | Valoare |
| ------ | ------ |
| Authorization | Token-ul de autentificare a autilizatorului logat|

Exemplu răspuns returnat pentru un scenariu de succes:

```JSON
{
    "data": {
        "header": [
            "Username", "Count", "Total amount", "Expenditure type"
        ],
        "entries": [
            ["John Doe", "56", "1", "BILL"],
            ["Karl Sorw", "300", "1", "BILL"],
            ["John Doe", "25", "1", "FOOD"],
            ["Karl Sorw", "75", "1", "FOOD"],
            ["John Doe", "1300", "2", "LARGE"],
            ["Karl Sorw", "100", "1", "MISC"]
        ]
    },
    "messages": [
        "Generated report data."
    ],
    "errorMessages": []
}
```

## 3. Implementare

### 3.1. Proiectarea bazei de date

S-a ales folosirea unei baze de date MySQL. Pentru conectarea la acesta s-a ales folosirea ORM-ului Hibernate. În acest mod pentru partea de modelare câmpurile entităților sunt adnotate specifi în funcției de maparea la o coloană corespondentă din tabelele bazei de date. Pe partea de tranzacționare și mapare a query-rilor se vor folosi componenete SpringBoot, și anume JPA Repositories.

### 3.1.1. Entități folosite

- _Alert_, stochează alertele aferente unui buget(e.x. alertă de depășire limită cheltuieli)
- _AuthJWT_, salvează tokenele JWT folosite pentru autentificare utilizatorilor în aplicație
- _BudgetRole_, salvează rolurile create în cadrul bugetelor din aplicație
- _Expenditure_, cuprinde detalii despre fiecare cheltuială efectuată(e.x. momentul de timp la care a fost introdusă, totalul cheltuit, utilizatorul care a introdus-o, bugetul de care aparține)
- _User_, a cărei entry-uri sunt identifcare unic de către email și UUID
- _DailyBudget_, conține detalii referitoare la fiecare buget creat în aplicație și anume ziua pentru care este creat și totatul care poate fi cheltuit

### 3.1.2. Diagrama bazei de date

![db diagram](https://github.com/MarcusGitAccount/Kor/blob/master/src/main/resources/images/bd_diagram.PNG)

## 3.2. Sistemul de alerte

În momentul în care sistemul încearcă să valideze adăugarea unei cheltuieli cu o sumă mai mare decât cea disponibilă la acel moment în bugetul aferent acesta va crea o alertă pe care o va salva în baza de date. În viitor această alertă va fi transmisă via email administratorilor bugetului.

### 3.2.1. Diagrama sistemului de alerte

După cum se poate vedea din diagrama acestuia, s-a folosit design pattern-ul Observer pentru a notifica componenta care se ocupă de crearea alertelor.

![alert system diagram](https://github.com/MarcusGitAccount/Kor/blob/master/src/main/resources/images/alert_system.PNG)

Este nevoie de o o componentă suplimentară pentru a lega și configura cele 2 servicii din cadrul pattern-ului implementat deoarece dorim să nu le instanțiem manual, ci să lăsăm framework-ul SpringBoot să se ocupe de creearea și managementul componentelor aplicației.

## 3.3 Sistemul de rapoarte

Acesta a fost implementat pentru a permite utilizatorilor care manipulează un buget să vizualizeze date agregate și statistici referitoare la acesta. La momentul actual, în aplicație sunt prezente următoarele tipurile de rapoarte:
- *CountSumByTypeReport*, permite vizualizarea numărului de cheltuieli și suma acestora în pentru fiecare tip de cheltuială
- *CountSumByTypeRoleReport*, permite vizualizarea numărului de cheltuieli și suma acestora pentru fiecare tip de cheltuială și utilizator care le-a adăugat

### 3.3.1. Query-uri de agregare

Pentru a obține datele pentru rapoarte, au fost implementate următoarele query-uri JPQL în repository-ul aferent entității _Expenditure_:

```Java
  @Query(value =
    "select new com.ps.kor.business.report.aggregation.CountSumByTypeAggregation(" +
    "e.type, sum(e.amount), count(e.amount)) " +
    "from Expenditure e " +
    "where e.dailyBudget = :budget " +
    "group by e.type "
  )
  List<CountSumByTypeAggregation> countAndSumByType(@Param("budget") DailyBudget budget);

  @Query(value =
      "select new com.ps.kor.business.report.aggregation.CountSumByTypeRoleAggregation(" +
          "concat(e.budgetRole.user.firstName, ' ', e.budgetRole.user.lastName), " +
          "e.type, sum(e.amount), count(e.amount)) " +
          "from Expenditure e " +
          "where e.dailyBudget = :budget " +
          "group by e.type, e.budgetRole"
  )
  List<CountSumByTypeRoleAggregation> countAndSumByTypeAndRole(@Param("budget") DailyBudget budget);
```

### 3.3.2. Diagrama sistemului de rapoarte

Pentru implementarea sistemului de rapoarte a fost folosit design pattern-ul Factory.

![report system diagram](https://github.com/MarcusGitAccount/Kor/blob/master/src/main/resources/images/report_system.PNG)

## 3.4. Diagrame

### 3.4.1. Login sequence diagram

![login sequence diagram](https://github.com/MarcusGitAccount/Kor/blob/master/src/main/resources/images/login_Sequence.PNG)


## 4. Testarea aplicației

Pentru aceasta s-a folosit Junit4 pentru testarea unitară a componentelor aplicației și Mockito pentru a imita datele și operațiile la nivel de bază de date.

Următorii pași au fost urmați:

1. Decuplarea logicii de la nivelul controller-ului în clase specializate pentru a elimina dependența de framework la nivelul testării unitare(au fost totuși scrise și efectuate și teste la nivel de controller care vor fi păstrate în continuare deoarece sunt util). Exemplu decuplare logică la nivel de controller: 

```Java
  @PostMapping("/login")
  public ResponseEntity login(@RequestBody User user) {
    BusinessMessage message = authLogic.login(user);
    return ResponseEntityFactory.createResponseFromBusinessMessage(message);
  }

```

2. Crearea unei configurări de test pentru a putea 'mock-ui' componente aplicației:
```Java
@Profile("testing")
@Configuration
public class TestingConfiguration {

  @Bean
  public UserRepo userRepo() {
    return Mockito.mock(UserRepo.class);
  }

  @Bean
  public DailyBudgetRepo dailyBudgetRepo() {
    return Mockito.mock(DailyBudgetRepo.class);
  }
 
  /* ..... */
 }
```

3. Imitarea aplicațiilor la nivelul de service layer. Pentru aceasta s-au folosit diferite containere pentru a simula operațiile pe baza de date. Exemplu:

```Java
 when(budgetRoleRepo.save(Mockito.any(BudgetRole.class)))
  .then(answer -> {
    BudgetRole role = answer.getArgument(0);

    if (testRoles != null) {
      testRoles.add(role);
    }
    return role;
  });
          
  when(budgetRoleRepo.findById(Mockito.any(UUID.class)))
  .then(answer -> {
    UUID id = answer.getArgument(0);

    if (testRoles == null) {
      return null;
    }
    return testRoles.stream()
        .filter(role -> role.getId().equals(id))
        .findAny();
  });
```

4. (Pasul final: profit :D). Apeluri către nivelul del business logic al aplicației și aserțiunile aferente validării acestora:

```Java
    @Test
    public void testCreateMethod() {
      DailyBudget budget = new DailyBudget();

      budget.setName("Test budget");
      BusinessMessage<DailyBudget> message = budgetLogic.create("token lol", budget);
      BudgetRole createdRole = null;

      assertEquals(1, testRoles.size()); // make sure only one instance was created
      createdRole = testRoles.get(0);
      assertEquals(testUser, createdRole.getUser());
      assertEquals(BudgetRoleType.CREATOR.rank, createdRole.getRoleType().rank);
    }
```

## 4.1. Testarea sistemului de raporte

Folosindu-se aceeași configurație de test, și aceeași logică și idee de a mockuire a componentelor aplicației, a fost scris următorul test pentru a asigura implementarea corectă a design pattern-ului Factory pe care sistemul de rapoarte îl folosește:

```Java
  @Test
  public void testFactoryCreation() {
    Report report1 = reportFactory.getInstance(ReportFactory.ReportType.CountAndSumByType);
    Report report2 = reportFactory.getInstance(ReportFactory.ReportType.CountAndSumByTypeRole);

    Assertions.assertTrue(report1 instanceof CountSumByTypeReport);
    Assertions.assertTrue(report2 instanceof CountSumByTypeRoleReport);
  }
```



















