package me.Cutiemango.MangoQuest.model;

public enum RequirementType{
	LEVEL("���ŻݨD", false), MONEY("�����ݨD", false),
	QUEST("���ȻݨD", true), ITEM("���~�ݨD", true), SCOREBOARD("�O���O���ƻݨD", true), NBTTAG("�O���OTag�ݨD", true);
	
	private String name;
	private boolean index;
	
	RequirementType(String s, boolean b){
		name = s;
		index = b;
	}
	
	public String toCustomString(){
		return name;
	}
	
	public boolean hasIndex(){
		return index;
	}
	
//	public static boolean vertify(RequirementType t, Object o){
//		switch (t){
//		case QUEST:
//			if (!(o instanceof List))
//				return false;
//		case LEVEL:
//			if (!(o instanceof Integer))
//				return false;
//		case MONEY:
//			if (!(o instanceof Double))
//				return false;
//		case ITEM:
//			if (!(o instanceof List))
//				return false;
//		case SCOREBOARD:
//			if (!(o instanceof List))
//				return false;
//		case NBTTAG:
//			if (!(o instanceof List))
//				return false;
//		}
//		return true;
//	}
}
