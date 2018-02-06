package cn.itcast.action;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import antlr.Utils;
import cn.itcast.common.SysConstant;
import cn.itcast.domain.User;
import cn.itcast.util.UtilFuns;

/**
 * @Description: 登录和退出类
 * @Author:		传智播客 java学院	传智.宋江
 * @Company:	http://java.itcast.cn
 * @CreateDate:	2014年10月31日
 */
public class LoginAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private String username;
	private String password;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	//SSH传统登录方式
	public String login() throws Exception {
		
		if (UtilFuns.isEmpty(username)) {
			return "login";
		}
		
		try {
			//1.得到subject
			Subject subject = SecurityUtils.getSubject();
			
			//2.调用登录方法
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			subject.login(token);  	// 当调用这行代码就会自动跳入到AuthRealm中认证的方法
			
			//3.登陆成功时，就从Shiro中取出用户的登录信息
			User user = (User) subject.getPrincipal();
			
			//4.将用户放入session中
			session.put(SysConstant.CURRENT_USER_INFO, user);
		} catch (Exception e) {
			e.printStackTrace();
			request.put("errorInfo", "对不起，你登录的用户名或密码错误！");
			return "login";
		}
		
		return SUCCESS;
	}
	
	
	//退出
	public String logout(){
		session.remove(SysConstant.CURRENT_USER_INFO);		//删除session
		
		return "logout";
	}

	

}

