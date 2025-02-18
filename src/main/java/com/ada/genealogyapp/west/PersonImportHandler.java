package com.ada.genealogyapp.west;

//@Component
//public class PersonImportHandler extends AbstractImportHandler<TreeImportJsonRequest, Map<String, Person>> {
//
//    private final PersonCreationService personCreationService;
//
//    public PersonImportHandler(ImportHandler<TreeImportJsonRequest, Map<String, Person>> nextHandler, PersonCreationService personCreationService) {
//        super(nextHandler);
//        this.personCreationService = personCreationService;
//    }
//
//    @Override
//    protected Map<String, Person> process(TreeImportJsonRequest input, Tree tree, Map<String, Object> context) {
//        Map<String, Person> personMap = new HashMap<>();
//        for (PersonRequest personRequest : input.getPersons()) {
//            Person person = personCreationService.createPerson(tree.getId(), personRequest);
//            personMap.put(personRequest.getId(), person);
//        }
//        context.put("personMap", personMap);
//        return personMap;
//    }
//}

