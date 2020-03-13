import java.io.*;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
public class BigDataComp {
public static void main(String[] args) {
 //this code produces 63% accurate results for 1010000 patient records
 String folderPath = "";
 String chemFilePath = folderPath + "ChemicalsRangeOptimizedTop20.csv";
 //RangeOptimized = the 10 most common symptoms have been removed
 String dataFilePath = folderPath + "CaseCompetitionPatientDataRangeOptimizedTop20.csv";
 //if the files don't exist, or there aren't file paths specified, return a usage message and get out
 if (chemFilePath.isEmpty() || !(new File(chemFilePath).exists()) || dataFilePath.isEmpty() || !(new File(dataFilePath).exists())) {
   System.out.println("Usage: matchChems [chemFilePath] [patientDataFilePath] [numberOfSymptoms]");
   return;
 }	
 int knum_patients = 1010000;
 int knum_chems = 311;
 int knum_symptoms = 79-20;
 HashMap<Integer, String[]> chem = new HashMap<Integer, String[]>();
 HashMap<Integer, double[]> patient = new HashMap<Integer,double[]>();
 HashMap<Integer, Integer> chemSymptomCounts = new HashMap<Integer, Integer>();	    
 try {
   Scanner chemFile = new Scanner(new File(chemFilePath));
   Scanner dataFile = new Scanner(new File(dataFilePath));
   String chemFileLine = "";
   String dataFileLine = "";
   int i = 1;
	    
   //read the chemical file in
   while(chemFile.hasNextLine()) {
     chemFileLine = chemFile.nextLine();
     //System.out.println(chemFileLine);
     String[] chemFileVector = chemFileLine.split(",");
     int chem_symptom_count = 0;
     for(int j = 0; j< chemFileVector.length; j++) {
       if(Integer.parseInt(chemFileVector[j]) == 1)
         chem_symptom_count++;
     }
     //put the count of symptoms for the chemical into the chemSymptomCounts hash table
     chemSymptomCounts.put(i, chem_symptom_count);
     //put the chemical and its symptoms into another hashtable for later comparison
     chem.put(i, chemFileVector);
     i = i + 1; 
   }
   chemFile.close();
   //does the symptom modifier run inside the patient loop, or outside it?
   //Outside the loop: 79 * 101000  
   int patient_no = 1;
   while(dataFile.hasNextLine()) {
     dataFileLine = dataFile.nextLine();
     String[] patientSymptoms = dataFileLine.split(",");
     double currMatch = 0; int best_match_id = 0; double best_match_percent = 0;
     double[] output = new double[2];
     for(int chemical = 1; chemical<= knum_chems; chemical++) {
       double patientSymptomCount = 0;
       double allSymptoms = 0;
       for(int symptom = 0; symptom < knum_symptoms; symptom++) {
         if(chem.get(chemical).length != patientSymptoms.length)
    	    System.out.println("The number of chemical symptoms != patient symptoms!");
    	    if(Integer.parseInt(patientSymptoms[symptom])== 1)
    	      allSymptoms++;
    	      if(Integer.parseInt(chem.get(chemical)[symptom]) == 1 && Integer.parseInt(patientSymptoms[symptom]) == 1)
    	        patientSymptomCount++;
    	 }
        currMatch = patientSymptomCount/chemSymptomCounts.get(chemical); //62% more accurate than below
    	//currMatch = patientSymptomCount/allSymptoms;
    	if(currMatch > best_match_percent) {
    	  best_match_percent = currMatch;
    	  best_match_id = chemical;
    	}
	output[0] = best_match_id;
	output[1] = best_match_percent;
     }
     patient.put(patient_no,output);
     patient_no = patient_no + 1;
    }
    dataFile.close();
    PrintWriter fileOut = new PrintWriter(new FileWriter("C:\\Users\\jackh\\OneDrive\\Desktop\\CaseComp\\testResults\\Top20Verification.csv"));
    fileOut.println("Matching Chemical");
    for(int k = 1; k < patient_no; k++) {
      //fileOut.println(patient.get(k)[0]);
      fileOut.println(k+","+ patient.get(k)[0]);
    }
    fileOut.close();
    }
    catch(Exception e) {
      System.out.println(e);
    }
    // System.out.println(patient.toString());
  }
}
