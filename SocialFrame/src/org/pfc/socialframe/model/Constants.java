package org.pfc.socialframe.model;

public class Constants {
	//Clave app de facebook
	public static final String APP_ID = "227586584004016";
	//Permisos de facebook
	public static final String [] perms = {"user_about_me","user_activities","user_birthday","user_education_history","user_events","user_games_activity",
		"user_groups","user_hometown","user_interests","user_likes","user_location","user_notes","user_online_presence","user_photo_video_tags",
		"user_photos","user_questions","user_relationship_details","user_relationships","user_religion_politics","user_status","user_subscriptions",
		"user_videos","user_website","user_work_history","friends_about_me","friends_activities","friends_birthday","friends_education_history",
		"friends_events","friends_games_activity","friends_groups","friends_hometown","friends_interests","friends_likes","friends_location","friends_notes",
		"friends_online_presence","friends_photo_video_tags","friends_photos","friends_questions","friends_relationship_details","friends_relationships",
		"friends_religion_politics","friends_status","friends_subscriptions","friends_videos","friends_website","friends_work_history","ads_management",
		"create_event","create_note","email","export_stream","manage_friendlists","manage_notifications","manage_pages","photo_upload","publish_actions",
		"publish_stream","read_friendlists","read_insights","read_mailbox","read_requests","read_stream","rsvp_event","share_item","sms","status_update","video_upload"};
	//Constantes para el activity Help
	public static final int FROMMAIN = 1;
	public static final int FROMREADER = 2;
	//Constantes de velocidad y distancia en los gestos de las fotos
	public static final int SWIPE_MIN_VELOCITY = 50;
	public static final int SWIPE_MIN_DISTANCE = 50;
	//Si la imagen viene de la camara o de la galeria
	public static final int TAKE_PICTURE = 1;
	public static final int CHOOSE_PICTURE = 2;
	//Maximo tamaño de una imagen a subir
	public static final int MAX_DIMENSION = 720;
	//Codigo para reconocer voz
	public static final int REQUEST_CODE = 1234;
	//Códigos QR
	public static final String[] EventsQR = {"calendario","fecha","blanco"};
	public static final String[] FeedQR = {"muro","pared","gris"};
	public static final String[] FriendsQR = {"agenda","contactos","verde"};
	public static final String[] InfoQR = {"carnet","tarjeta","azul"};
	public static final String[] MessagesQR = {"carta","mensaje","amarillo"};
	public static final String[] PhotosQR = {"foto","album","negro"};
	//Matriz con paises ISO
	public static final String[][] Countries = {{"AD","AE","AF","AG","AI","AL","AM","AN","AO","AQ","AR","AS","AT","AU","AW","AX","AZ",
		"BA","BB","BD","BE","BF","BG","BH","BI","BJ","BL","BM","BN","BO","BR","BS","BT","BV","BW","BY","BZ","CA","CC","CF","CG","CH",
		"CI","CK","CL","CM","CN","CO","CR","CU","CV","CX","CY","CZ","DE","DJ","DK","DM","DO","DZ","EC","EE","EG","EH","ER","ES","ET",
		"FI","FJ","FK","FM","FO","FR","GA","GB","GD","GE","GF","GG","GH","GI","GL","GM","GN","GP","GQ","GR","GS","GT","GU","GW","GY",
		"HK","HM","HN","HR","HT","HU","ID","IE","IL","IM","IN","IO","IQ","IR","IS","IT","JE","JM","JO","JP","KE","KG","KH","KI","KM",
		"KN","KP","KR","KW","KY","KZ","LA","LB","LC","LI","LK","LR","LS","LT","LU","LV","LY","MA","MC","MD","ME","MG","MH","MK","ML",
		"MM","MN","MO","MQ","MR","MS","MT","MU","MV","MW","MX","MY","MZ","NA","NC","NE","NF","NG","NI","NL","NO","NP","NR","NU","NZ",
		"OM","PA","PE","PF","PG","PH","PK","PL","PM","PN","PR","PS","PT","PW","PY","QA","RE","RO","RS","RU","RW","SA","SB","SC","SD",
		"SE","SG","SH","SI","SJ","SK","SL","SM","SN","SO","SR","ST","SV","SY","SZ","TC","TD","TF","TG","TH","TH","TJ","TK","TL","TM",
		"TN","TO","TR","TT","TV","TW","UA","UG","US","UY","UZ","VA","VC","VE","VG","VI","VN","VU","WF","WS","YE","YT","ZA"},{"Andorra",
		"Emiratos Árabes Unidos","Afganistán","Antigua y Barbuda","Anguila","Albania","Armenia","Antillas Neerlandesas","Angola","Antártida",
		"Argentina","Samoa Americana","Austria","Australia","Aruba","I. Áland","Azerbaiyán","Bosnia y Herzegovina","Barbados","Bangladesh",
		"Bélgica","Burkina Faso","Bulgaria","Bahréin","Burundi","Benin","San Bartolomé","Bermudas","Brunéi","Bolivia","Brasil","Bahamas",
		"Bhután","I. Bouvet","Botsuana","Belarús","Belice","Canadá","I. Cocos","Rep. Centro-Africana","Congo","Suiza","Costa de Marfil",
		"I. Cook","Chile","Camerún","China","Colombia","Costa Rica","Cuba","Cabo Verde","I. Christmas","Chipre","Rep. Checa","Alemania",
		"Yibuti","Dinamarca","Domínica","Rep. Dominicana","Argel","Ecuador","Estonia","Egipto","Sahara","Eritrea","España","Etiopía","Finlandia",
		"Fiji","I. Malvinas","Micronesia","I. Feroe","Francia","Gabón","Reino Unido","Granada","Georgia","Guayana Francesa","Guernsey","Ghana",
		"Gibraltar","Groenlandia","Gambia","Guinea","Guadalupe","Guinea Ecuatorial","Grecia","Georgia del Sur","Guatemala","Guam","Guinea-Bissau",
		"Guayana","Hong Kong","I. Heard y McDonald","Honduras","Croacia","Haití","Hungría","Indonesia","Irlanda","Israel","I. de Man","India",
		"T.B. Océano Índico","Irak","Irán","Islandia","Italia","Jersey","Jamaica","Jordania","Japón","Kenia","Kirguistán","Camboya","Kiribati",
		"Comoros","San Cristóbal","Corea del Norte","Corea del Sur","Kuwait","I. Caimán","Kazajstán","Laos","Líbano","Santa Lucía","Liechtenstein",
		"Sri Lanka","Liberia","Lesotho","Lituania","Luxemburgo","Letonia","Libia","Marruecos","Mónaco","Moldavia","Montenegro","Madagascar","I. Marshall",
		"Macedonia","Mali","Myanmar","Mongolia","Macao","Martinica","Mauritania","Montserrat","Malta","Mauricio","Maldivas","Malawi","México","Malasia",
		"Mozambique","Namibia","Nueva Caledonia","Níger","I. Norkfolk","Nigeria","Nicaragua","Países Bajos","Noruega","Nepal","Nauru","Niue","Nueva Zelanda",
		"Omán","Panamá","Perú","Polinesia Francesa","Papúa Nueva Guinea","Filipinas","Pakistán","Polonia","San Pedro y Miquelón","I. Pitcairn","Puerto Rico",
		"Palestina","Portugal","I. Palaos","Paraguay","Qatar","Reunión","Rumanía","Serbia","Rusia","Ruanda","Arabia Saudita","I. Solomón","Seychelles","Sudán",
		"Suecia","Singapur","Santa Elena","Eslovenia","I. Svalbard","Eslovaquia","Sierra Leona","San Marino","Senegal","Somalia","Surinam","Santo Tomé y Príncipe",
		"El Salvador","Siria","Suazilandia","I. Turcas y Caicos","Chad","T. Australes Franceses","Togo","Tailandia","Tanzania","Tayikistán","Tokelau","Timor-Leste",
		"Turkmenistán","Túnez","Tonga","Turquía","Trinidad y Tobago","Tuvalu","Taiwán","Ucrania","Uganda","EEUU","Uruguay","Uzbekistán","Vaticano","San Vicente",
		"Venezuela","I. Vírgenes Británicas","I. Vírgenes EEUU","Vietnam","Vanuatu","Wallis y Futuna","Samoa","Yemen","Mayotte","Sudáfrica"}};
}