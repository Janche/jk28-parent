package cn.itcast.action.sysadmin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.action.BaseAction;
import cn.itcast.domain.Dept;
import cn.itcast.domain.Role;
import cn.itcast.domain.User;
import cn.itcast.service.DeptService;
import cn.itcast.service.RoleService;
import cn.itcast.service.UserService;
import cn.itcast.util.Page;

/**
 * 部门管理的Action
 * 
 * @author LR-PC
 */
public class UserAction extends BaseAction implements ModelDriven<User> {

	private static final long serialVersionUID = 1L;
	private User model = new User();

	@Override
	public User getModel() {
		return model;
	}

	// 分页查询
	private Page<User> page = new Page<User>();

	public Page<User> getPage() {
		return page;
	}

	public void setPage(Page<User> page) {
		this.page = page;
	}

	private String[] roleIds;

	public String[] getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String[] roleIds) {
		this.roleIds = roleIds;
	}

	// 注入userService
	private UserService userService;

	public void setuserService(UserService userService) {
		this.userService = userService;
	}

	// 注入deptService
	private DeptService deptService;

	public void setDeptService(DeptService deptService) {
		this.deptService = deptService;
	}

	// 注入roleService
	private RoleService roleService;

	public void setroleService(RoleService roleService) {
		this.roleService = roleService;
	}

	/**
	 * 分页查询
	 */
	public String list() throws Exception {
		// 此处的page = 可以写出来，也可不写，因为修改的引用 类型的
		page = userService.findPage("from User", page, User.class, null);
		// 设置分页的URL
		page.setUrl("userAction_list");

		// 将page对象压入栈顶
		super.push(page);
		return "list";
	}

	/**
	 * 查看
	 */
	public String toview() {
		// 1.调用业务方法，根据id,得到对象
		User user = userService.get(User.class, model.getId());
		// 放入栈顶
		super.push(user);
		return "toview";
	}

	/**
	 * 新增
	 */
	public String tocreate() {
		// 调用业务方法
		List<Dept> deptList = deptService.find("from Dept where state=1", Dept.class, null);
		List<User> userList = userService.find("from User where state=1", User.class, null);
		// 将查询结果放入值栈中 单个对象用push，集合用下面这个
		super.put("userList", userList);
		super.put("deptList", deptList);
		// 跳页面
		return "tocreate";
	}

	/**
	 * 保存
	 */
	public String insert() {
		// 调用业务方法，实现保存
		userService.saveOrUpdate(model);
		// 跳页面
		return "alist";
	}

	/**
	 * 进入修改页面
	 */
	public String toupdate() {
		// 根据id，得到一个对象
		User obj = userService.get(User.class, model.getId());
		// 将对象放入值栈中
		super.push(obj);
		// 查询父部门
		List<Dept> deptList = deptService.find("from Dept where state=1", Dept.class, null);
		super.put("deptList", deptList);
		// 跳页面
		return "toupdate";
	}

	/**
	 * 修改
	 */
	public String update() {
		// 根据id，得到一个数据库中保存的对象
		User obj = userService.get(User.class, model.getId());
		// 设置修改的属性
		obj.setDept(model.getDept());
		obj.setUserName(model.getUserName());
		obj.setState(model.getState());
		userService.saveOrUpdate(obj);
		// 跳页面
		return "alist";
	}

	/**
	 * 删除 model id:String类型 具有同名框的一组值如何封装数据 如果服务器端是String类型： Struts2默认采用 , 分割
	 * id:Integer类型：100 200 300 将会导致数据丢失，Struts2默认只保存最后一个 Integer[]id;
	 * {100,200,300}服务器端用数组来接受没有问题
	 * 
	 * @return
	 * @throws Exception
	 */
	public String delete() throws Exception {
		String[] ids = model.getId().split(",");
		userService.delete(User.class, ids);
		return "alist";
	}

	/**
	 * 为用户添加角色
	 * 
	 * @return
	 * @throws Exception
	 */
	public String torole() throws Exception {
		// 查询用户
		User obj = userService.get(User.class, model.getId());
		super.push(obj);
		// 获取所有角色列表
		List<Role> roleList = roleService.find("from Role", Role.class, null);
		super.put("roleList", roleList);
		// 获取用户所拥有的角色
		Set<Role> roles = obj.getRoles();
		StringBuffer sb = new StringBuffer();
		for (Role role : roles) {
			sb.append(role.getName()).append(",");
		}
		super.put("roleStr", sb);
		return "torole";
	}

	public String role() throws Exception {
		// 获取用户
		User user = userService.get(User.class, model.getId());
		// 获取选中的角色
		Set<Role> roles = new HashSet<>();
		for (String roleId : roleIds) {
			Role role = roleService.get(Role.class, roleId);
			roles.add(role);
		}
		user.setRoles(roles);
		// 保存用户
		userService.saveOrUpdate(user);
		return "alist";
	}
}
