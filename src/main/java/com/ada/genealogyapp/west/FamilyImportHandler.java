package com.ada.genealogyapp.west;

//@Component
//public class FamilyImportHandler extends AbstractImportHandler<TreeImportJsonRequest, Map<String, Family>> {
//
//    private final FamilyCreationService familyCreationService;
//    private final FamilyService familyService;
//
//    public FamilyImportHandler(ImportHandler<TreeImportJsonRequest, Map<String, Family>> nextHandler, FamilyCreationService familyCreationService, FamilyService familyService) {
//        super(nextHandler);
//        this.familyCreationService = familyCreationService;
//        this.familyService = familyService;
//    }
//
//    @Override
//    protected Map<String, Family> process(TreeImportJsonRequest input, Tree tree, Map<String, Object> context) {
//        Map<String, Family> familyMap = new HashMap<>();
//        Map<String, Person> personMap = (Map<String, Person>) context.get("personMap");
//        Map<String, Participant> participantMap = new HashMap<>(personMap);
//
//        for (FamilyRequest familyRequest : input.getFamilies()) {
//            Family family = familyCreationService.createFamily(tree.getId(), familyRequest);
//            addParentsToFamily(family, familyRequest, personMap);
//            addChildrenToFamily(family, familyRequest, personMap);
//            familyService.saveFamily(family);
//
//            familyMap.put(familyRequest.getId(), family);
//            participantMap.put(familyRequest.getId(), family);
//        }
//
//        context.put("familyMap", familyMap);
//        context.put("participantMap", participantMap);
//        return familyMap;
//    }
//
//    private void addParentsToFamily(Family family, FamilyRequest familyRequest, Map<String, Person> personMap) {
//    }
//
//    private void addChildrenToFamily(Family family, FamilyRequest familyRequest, Map<String, Person> personMap) {
//    }
//}

