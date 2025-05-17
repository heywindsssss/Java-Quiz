import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.NoSuchFileException;

class Property {
	private String question;
	private char option;
	private String answer;

	public void setQuestion(String question) {
		this.question = question;
	}
	public void setOption(char option) {
		this.option = option;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getQuestion () {
		return this.question;
	}
	public char getOption () {
		return this.option;
	}
	public String getAnswer () {
		return this.answer;
	}
}

class Options {
	private String option1,option2,option3,option4;
	private String allOptions;
	public void setAllOptions(String allOptions) {
		this.allOptions = allOptions;
	}
	public void setOption1(String option1) {
		this.option1 =  option1;
	}
	public void setOption2(String option2) {
		this.option2 =  option2;
	}
	public void setOption3(String option3) {
		this.option3 =  option3;
	}
	public void setOption4(String option4) {
		this.option4 =  option4;
	}

	public String getOption1() {
		return option1;
	}
	public String getOption2() {
		return option2;
	}
	public String getOption3() {
		return option3;
	}
	public String getOption4() {
		return option4;
	}
}

class ReadFile {
	private String filePath;
	private String content;
	public ReadFile(String filePath) {
		this.filePath = filePath;
		try {
			this.content = new String(Files.readAllBytes(Paths.get(filePath)));
		} catch (NoSuchFileException e) {
			System.out.println("Error: File not found at " + filePath);
			this.content = null;
		} catch (IOException e) {
			System.out.println("An error occurred while reading the file: " + e.getMessage());
			this.content = null;
		} catch (Exception e) {
			System.out.println("An unexpected error occurred: " + e.getMessage());
			this.content = null;
		}
	}
	public String getContent() {
		return content;
	}
}

//Try to make this class more cleaner and readable
class FiftyFifty {
	private String correctAnswerText;
	private Map<Character, String> optionMap;
	private ArrayList<String> remainingOptions;

	public FiftyFifty(Property property, Options options) {
		this.optionMap = new HashMap<>();
		this.remainingOptions = new ArrayList<>();

		optionMap.put('a', options.getOption1());
		optionMap.put('b', options.getOption2());
		optionMap.put('c', options.getOption3());
		optionMap.put('d', options.getOption4());

		char correctOptionLetter = property.getOption();
		this.correctAnswerText = optionMap.get(correctOptionLetter);

		useFiftyFifty(correctOptionLetter);
	}

	public void useFiftyFifty(char correctOptionLetter) {
		remainingOptions.add(correctOptionLetter + ") " + correctAnswerText);

		ArrayList<Map.Entry<Character, String>> incorrectEntries = new ArrayList<>();
		for (Map.Entry<Character, String> entry : optionMap.entrySet()) {
			if (entry.getKey() != correctOptionLetter) {
				incorrectEntries.add(entry);
			}
		}

		Random random = new Random();
		Map.Entry<Character, String> randomIncorrect = incorrectEntries.get(random.nextInt(incorrectEntries.size()));
		remainingOptions.add(randomIncorrect.getKey() + ") " + randomIncorrect.getValue());
	}

	public ArrayList<String> getModifiedOptions() {
		return remainingOptions;
	}
}

class CheckForInvalidInput {
	private Set<Character> setOfValidInputs;
	private Scanner scanner;

	public CheckForInvalidInput() {
		this.setOfValidInputs = new HashSet<>();
		this.scanner = new Scanner(System.in);
		setOfValidInputs.add('a');
		setOfValidInputs.add('b');
		setOfValidInputs.add('c');
		setOfValidInputs.add('d');
	}

	public char checking(boolean isFiftyFiftyUsed, boolean isPhoneAFriendUsed) {
		Set<Character> currentValidInputs = new HashSet<>(setOfValidInputs);
		if (!isFiftyFiftyUsed) {
			currentValidInputs.add('e');
		}
		if (!isPhoneAFriendUsed) {
			currentValidInputs.add('f');
		}

		System.out.println("Enter your choice: ");
		String input = scanner.nextLine().trim().toLowerCase();

		if (input.length() == 1 && currentValidInputs.contains(input.charAt(0))) {
			return input.charAt(0);
		} else {
			System.out.println("Invalid input: Please Enter Choice from given options");
			return checking(isFiftyFiftyUsed, isPhoneAFriendUsed); // Recursive call
		}
	}
}

class PhoneAFriend {
	private String correctAnswerText;
	private char correctOptionLetter;

	public PhoneAFriend(Property property) {
		this.correctAnswerText = property.getAnswer();
		this.correctOptionLetter = property.getOption();
	}

	public String getFriendAdvice() {
		return ("The correct option is " + correctOptionLetter + ") " + correctAnswerText);
	}
}

class Quiz {
	private String queAnsFilePath;
	private String allOptionsFilePath;
	private String gameName;
	private Scanner scanner;
	private ArrayList<Property> properties; // Naming should be better
	private ArrayList<Options> options;
	private CheckForInvalidInput inputChecker;

	public Quiz (String queAnsFilePath, String allOptionsFilePath, String gameName) {
		this.queAnsFilePath = queAnsFilePath;
		this.allOptionsFilePath = allOptionsFilePath;
		this.properties =  new ArrayList<Property>();
		this.options =  new ArrayList<Options>();
		this.inputChecker = new CheckForInvalidInput();
		this.scanner = new Scanner(System.in);
		this.gameName = gameName;
		System.out.println("Let's start the game of " + gameName);
	}

	private void loadQuestions() {
		ReadFile queAnsFile = new ReadFile(queAnsFilePath);
		String str = queAnsFile.getContent();

		String[] splittedQueAnsOption =  str.split("llllllooooopppppp");
		int splittedArrayLength = splittedQueAnsOption.length;
		for (int i = 0; i < splittedArrayLength; i++) {
			String subArrayString = splittedQueAnsOption[i];
			String part = subArrayString.substring(1, subArrayString.length() - 1);
			String[] content = part.split(",");
			if(content.length == 3) {
				Property current =  new Property();
				current.setQuestion(content[0].trim());
				if (content[1].trim().length() == 1) {
					current.setOption(content[1].trim().charAt(0));
				} else {
					System.out.println("Warning: Option format is incorrect for entry: " + subArrayString);
				}
				current.setAnswer(content[2].trim());
				properties.add(current);
			}
			else {
				System.out.println("Invalid length");
			}
		}
	}

	private void loadOptions() {
		ReadFile allOptionsFile = new ReadFile(allOptionsFilePath);
		String allOptionsStr = allOptionsFile.getContent();

		String[] splittedAllOptions =  allOptionsStr.split("llllllooooopppppp");
		int splittedAllOptionsLength = splittedAllOptions.length;
		for (int i = 0; i < splittedAllOptionsLength; i++) {
			String subArrayOptionString = splittedAllOptions[i];
			String cleanedOptionsPart = subArrayOptionString.trim();
			if (cleanedOptionsPart.startsWith("[") && cleanedOptionsPart.endsWith("]")) {
				cleanedOptionsPart = cleanedOptionsPart.substring(1, cleanedOptionsPart.length() - 1).trim();
			}
			String[] individualOptions = cleanedOptionsPart.split("\\.\\s*");
			if (individualOptions.length == 4) {
				Options allOption = new Options();
				allOption.setOption1(individualOptions[0].substring(2).trim());
				allOption.setOption2(individualOptions[1].substring(2).trim());
				allOption.setOption3(individualOptions[2].substring(2).trim());
				allOption.setOption4(individualOptions[3].substring(2).trim());
				allOption.setAllOptions(cleanedOptionsPart);
				options.add(allOption);
			} else {
				System.out.println("Warning: Could not parse options for entry: " + subArrayOptionString);
			}
		}
	}

	private void loadQuestionsAndOptions() {
		loadOptions();
		loadQuestions();
	}

	private void displayQuestion(Property property, Options optionSet, boolean isFiftyFiftyUsed, boolean isPhoneAFriendUsed) {
		System.out.println("Question: " + property.getQuestion());
		System.out.println("Options:");
		System.out.println("a) " + optionSet.getOption1());
		System.out.println("b) " + optionSet.getOption2());
		System.out.println("c) " + optionSet.getOption3());
		System.out.println("d) " + optionSet.getOption4());
		displayLifelineOptions(isFiftyFiftyUsed, isPhoneAFriendUsed);
	}

	private void displayLifelineOptions(boolean isFiftyFiftyUsed, boolean isPhoneAFriendUsed) {
		if (!isFiftyFiftyUsed) {
			System.out.println("e) Use the fifty-fifty lifeline (once).");
		}
		if (!isPhoneAFriendUsed) {
			System.out.println("f) Use the Phone-A-Friend lifeline (once).");
		}
	}

	private void handleFiftyFifty(Property property, Options optionSet) {
		FiftyFifty fiftyFifty = new FiftyFifty(property, optionSet);
		ArrayList<String> modifiedOptions = fiftyFifty.getModifiedOptions();
		System.out.println("Options after using 50-50:");
		for (String option : modifiedOptions) {
			System.out.println(option);
		}
		System.out.println("Enter your answer:");
	}

	private void handlePhoneAFriend(Property property) {
		PhoneAFriend phoneAFriend = new PhoneAFriend(property);
		String friendsAdvice = phoneAFriend.getFriendAdvice();
		System.out.println(friendsAdvice);
		System.out.println("Enter your answer:");
	}

	private boolean checkForCorrection(char userResponse, char actualOption, String actualAnswer, int score) {
		if(userResponse == actualOption) {
			System.out.println("Correct answer !!! " + actualOption + ") " + actualAnswer);
			return true;
		}
		else {
			System.out.println("You entered wrong answer !!! Exiting the game. Your final score is: "+ score +", The correct answer was " + actualOption +")"+ actualAnswer);
			return false;
		}
	}
	
	private void displayFinalScore(int score){
	    System.out.println("Your final score was: " + score);
	}

	public void run() {
		loadQuestionsAndOptions();
		int score = 0;
		boolean isFiftyFiftyUsed =  false;
		boolean isPhoneAFriendUsed = false;
		if (properties.size() == options.size()) {
			for (int i = 0; i < properties.size(); i++) {
				Property property = properties.get(i);
				Options optionSet = options.get(i);
				displayQuestion(property, optionSet, isFiftyFiftyUsed, isPhoneAFriendUsed);
				char userAnswer = inputChecker.checking(isFiftyFiftyUsed, isPhoneAFriendUsed);
				if (userAnswer == 'e' && !isFiftyFiftyUsed) {
					handleFiftyFifty(property,optionSet);
					userAnswer = scanner.nextLine().trim().toLowerCase().charAt(0);
					isFiftyFiftyUsed = true;
				}
				if (userAnswer == 'f' && !isPhoneAFriendUsed) {
					handlePhoneAFriend(property);
					userAnswer = scanner.nextLine().trim().toLowerCase().charAt(0);
					isPhoneAFriendUsed = true;
				}
				if (checkForCorrection(userAnswer, property.getOption(), property.getAnswer(), score)) {
					score++;
					System.out.println();
					if (i == properties.size() - 1) {
						displayFinalScore(score);
					}
				} else {
					break; 
				}
				System.out.println();
				if(i == properties.size()-1) {
					System.out.println("Wohoo you have answered all questions correctly. Your final score is: "+ score);
				}
			}
		} else {
			System.out.println("Error: The number of questions and option sets do not match. " +
			                   "Questions: " + properties.size() + ", Option Sets: " + options.size());
		}
	}
}

public class Main
{
	public static void main(String[] args) {
		Quiz quiz = new Quiz("queAns.txt","allOptions.txt", "KBC");
		quiz.run();
	}
}
