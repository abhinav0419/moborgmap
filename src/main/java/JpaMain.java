//
//
//import java.util.List;
//
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//
//
//import com.mobiquity.snack.model.UserEntity;
//import com.mobiquity.snack.service.UserService;
//
//
//
//public class JpaMain {
//
//	public static void main(String[] args) {
//		try{
//			
//			@SuppressWarnings("resource")
//            ApplicationContext context = new ClassPathXmlApplicationContext(
//					"onramp-dao-infrastructure.xml", 
//					"onramp-daos.xml");
//			UserEntity user1 = new UserEntity();
//			user1.setActive(true);
//			user1.setFirstName("ravi");
//			user1.setLastName("nair");
//			user1.setMiddleName("g");
//			user1.setSalt("abc4334Df");
//			user1.setPassword("sadsa");
//			user1.setUserName("rav21@gmail.com");
//			
////			UserEntity user = new UserEntity();
////			user.setAdmin(true);
////			user.setFirstName("Bhavin");
////			user.setLastName("Rajani");
////			user.setFullName("Bhavin Rajani");
////			
////			user.setManager(true);
////			user.setRegion("LA");
////			user.setRole("LA-Manager");
////			user.setSalesForceId(1);
////			
//			UserService service = context.getBean(UserService.class);
//			List<UserEntity> userList=service.getAllUser();
//			for(UserEntity list: userList){
//				System.out.println("First Name " +list.getFirstName());
//			}
//			
//		}catch(Exception exc) {
//			exc.printStackTrace();
//		}
//	}
//}
