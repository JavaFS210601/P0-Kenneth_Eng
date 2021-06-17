package journal;

import java.util.Date;

public class JournalDao implements JournalDaoImpl {
	Journal[] journals;
	JournalDao(){
		retrieveAllJournals();
	}
	
	public Journal[] retrieveAllJournals() {
		journals = new Journal[5];
		String[] arr = {" At w3schools.com you will learn how to make a website. \n "
				+ "Contented get distrusts certainty nay are frankness concealed ham. \n"
				+ "On unaffected resolution on considered of. No thought me husband or colonel forming effects. \n"
				+ "End sitting shewing who saw besides son musical adapted. Contrasted interested eat \n"
				+ "alteration pianoforte sympathize was. He families believed if no elegance interest surprise an. \n"
				+ "It abode wrong miles an so delay plate. She relation own put outlived may disposed. " , 
				"Why end might ask civil again spoil. She dinner she our horses depend. Remember at children \n"
				+ "by reserved to vicinity. In affronting unreserved delightful simplicity ye. Law own advantage \n"
				+ "furniture continual sweetness bed agreeable perpetual. Oh song well four only head busy it. \n"
				+ " Afford son she had lively living. Tastes lovers myself too formal season our valley boy. \n"
				+ "Lived it their their walls might to by young. "};
		String[] arr1 = {"this is article 1." + arr[0], arr[1]} ; 
		String[] arr2 = {"this is article 2." + arr[0], arr[1]} ; 
		String[] arr3 = {"this is article 3." + arr[0], arr[1]} ; 
		String[] arr4 = {"this is article 4." + arr[0], arr[1]} ;
		String[] arr5 = {"this is article 5." + arr[0], arr[1]} ; ;
		Journal j =  new Journal("Article 1","Kenneth Eng", new Date(), arr1);
		Journal j1 =  new Journal("Article 2","Kenneth Eng", new Date(), arr2);
		Journal j2 =  new Journal("Article 3","Kenneth Eng", new Date(), arr3);
		Journal j3 =  new Journal("Article 4","Kenneth Eng", new Date(), arr4);
		Journal j4 =  new Journal("Article 5","Kenneth Eng", new Date(), arr5);
		journals[0] = j;
		journals[1] = j1;
		journals[2] = j2;
		journals[3] = j3;
		journals[4] = j4;
		return journals;
	}
	
	public Journal getJournalById(int id) {
		return journals[id];
	}
}
