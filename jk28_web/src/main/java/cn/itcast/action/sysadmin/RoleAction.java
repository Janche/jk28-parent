package cn.itcast.action.sysadmin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.action.BaseAction;
import cn.itcast.domain.DictAndModule;
import cn.itcast.domain.Dictionary;
import cn.itcast.domain.Module;
import cn.itcast.domain.Role;
import cn.itcast.exception.SysException;
import cn.itcast.service.DictAndModuleService;
import cn.itcast.service.ModuleService;
import cn.itcast.service.RoleService;
import cn.itcast.service.UserService;
import cn.itcast.util.Encrypt;
import cn.itcast.util.Page;

/**
 * 部门管理的Action
 * 
 * @author LR-PC
 */
public class RoleAction extends BaseAction implements ModelDriven<Role> {

	private static final long serialVersionUID = 1L;
	private Role model = new Role();

	@Override
	public Role getModel() {
		return model;
	}

	// 分页查询
	private Page<Role> page = new Page<Role>();

	public Page<Role> getPage() {
		return page;
	}

	public void setPage(Page<Role> page) {
		this.page = page;
	}

	private String moduleIds;

	public void setModuleIds(String moduleIds) {
		this.moduleIds = moduleIds;
	}

	// 注入UserService
	private RoleService roleService;

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	// 注入UserService
	private UserService userService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	// 注入moduleService
	private ModuleService moduleService;

	public void setModuleService(ModuleService moduleService) {
		this.moduleService = moduleService;
	}

	// 注入dictAndModuleService
	private DictAndModuleService dictModuleService;
	public void setDictModuleService(DictAndModuleService dictModuleService) {
		this.dictModuleService = dictModuleService;
	}

	/**
	 * 分页查询
	 */
	public String list() throws Exception {
		// 此处的page = 可以写出来，也可不写，因为修改的引用 类型的
		page = roleService.findPage("from Role", page, Role.class, null);
		// 设置分页的URL
		page.setUrl("roleAction_list");

		// 将page对象压入栈顶
		super.push(page);
		return "list";
	}

	/**
	 * 查看
	 * 
	 * @throws SysException
	 */
	public String toview() throws SysException {
		try {
			// 1.调用业务方法，根据id,得到对象
			Role Role = roleService.get(Role.class, model.getId());
			// 放入栈顶
			super.push(Role);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SysException("对不起，你的操作有误，请先勾选！");
		}
		return "toview";
	}

	/**
	 * 新增
	 */
	public String tocreate() {
		// 调用业务方法
		List<Role> roleList = roleService.find("from Role ", Role.class, null);
		// 将查询结果放入值栈中 单个对象用push，集合用下面这个
		super.put("roleList", roleList);
		// 跳页面
		return "tocreate";
	}

	/**
	 * 保存
	 */
	public String insert() {
		// 调用业务方法，实现保存
		roleService.saveOrUpdate(model);
		// 跳页面
		return "alist";
	}

	/**
	 * 进入修改页面
	 */
	public String toupdate() {
		// 根据id，得到一个对象
		Role obj = roleService.get(Role.class, model.getId());
		// 将对象放入值栈中
		super.push(obj);
		// 跳页面
		return "toupdate";
	}

	/**
	 * 修改
	 */
	public String update() {
		// 根据id，得到一个数据库中保存的对象
		Role obj = roleService.get(Role.class, model.getId());
		// 设置修改的属性
		obj.setName(model.getName());
		obj.setRemark(model.getRemark());
		roleService.saveOrUpdate(obj);
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
		roleService.delete(Role.class, ids);
		return "alist";
	}

	/**
	 * 进入模块页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String tomodule() throws Exception {
		Role role = roleService.get(Role.class, model.getId());
		// 保存到值栈中
		super.push(role);

		return "tomodule";
	}

	/**
	 * 为页面准备json数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public String roleModuleJsonStr() throws Exception {
		// 获取角色对应的模块
		Role role = roleService.get(Role.class, model.getId());
		Set<Module> modules = role.getModules();
		// 获取所有模块
		List<Module> list = moduleService.find("from Module", Module.class, null);
		// 拼接页面所需要的JSON
		// [{"id":"模块的id","pId":"父模块id","name":"模块名","checked":"true|false"},{"id":"模块的id","pId":"父模块id":"模块名","checked":"true|false"}]
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		int count = list.size();
		for (Module module : list) {
			Set<Dictionary> dicts = module.getDicts();
			StringBuilder str = new StringBuilder();
			for (Dictionary dict : dicts) {
				str.append(dict.getName()).append(",");
			}
			String operates = str.toString();
			count--;
			sb.append("{\"id\":\"").append(module.getId());
			sb.append("\",\"pId\":\"").append(module.getParentId());
			sb.append("\",\"name\":\"").append(module.getName());
			sb.append("\",\"checked\":\"");
			if (modules.contains(module)) {
				sb.append("true");
			} else {
				sb.append("false");
			}

			if (module.getLayerNum() > 1) {

				sb.append("\",children:[");
				sb.append("{\"id\":\"").append("operated");
				sb.append("\",\"pId\":\"").append(module.getId());
				sb.append("\",\"name\":\"").append("查看");
				sb.append("\",\"checked\":\"");
				if (operates.contains("查看")) {
					sb.append("true");
				} else {
					sb.append("false");
				}
				sb.append("\"}");
				sb.append(",");

				sb.append("{\"id\":\"").append("operatea");
				sb.append("\",\"pId\":\"").append(module.getId());
				sb.append("\",\"name\":\"").append("新增");
				sb.append("\",\"checked\":\"");
				if (operates.contains("新增")) {
					sb.append("true");
				} else {
					sb.append("false");
				}
				sb.append("\"}");
				sb.append(",");

				sb.append("{\"id\":\"").append("operateb");
				sb.append("\",\"pId\":\"").append(module.getId());
				sb.append("\",\"name\":\"").append("修改");
				sb.append("\",\"checked\":\"");
				if (operates.contains("修改")) {
					sb.append("true");
				} else {
					sb.append("false");
				}
				sb.append("\"}");
				sb.append(",");

				sb.append("{\"id\":\"").append("operatec");
				sb.append("\",\"pId\":\"").append(module.getId());
				sb.append("\",\"name\":\"").append("删除");
				sb.append("\",\"checked\":\"");
				if (operates.contains("删除")) {
					sb.append("true");
				} else {
					sb.append("false");
				}
				sb.append("\"}");
				sb.append(",");

				sb.append("{\"id\":\"").append("operatee");
				sb.append("\",\"pId\":\"").append(module.getId());
				sb.append("\",\"name\":\"").append("权限");
				sb.append("\",\"checked\":\"");
				if (operates.contains("权限")) {
					sb.append("true");
				} else {
					sb.append("false");
				}
				sb.append("\"}");

				sb.append("]");

			} else {
				sb.append("\"");
			}
			sb.append("}");
			if (count > 0) {
				sb.append(",");
			}
		}
		sb.append("]");

		// 获取response对象
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Cache-control", "no-cache");
		response.getWriter().write(sb.toString());
		;

		return NONE;
	}

	/**
	 * 保存页面的权限模块操作关联数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public String module() throws Exception {
		Role role = roleService.get(Role.class, model.getId());
		System.out.println("moduleIds:" + moduleIds);
		List<String[]> sp = Encrypt.split(moduleIds);
		StringBuilder sb = new StringBuilder();
		for (String[] str : sp) {
			sb.append(str[0]).append(",");
			if (str.length > 1) {
				for (int i = 0; i < str.length - 1; i++) {
					DictAndModule dm = new DictAndModule();
					dm.setRoleId(model.getId());
					// 查询dictAndModule中是否存在与role相关的数据
					/*List<DictAndModule> list = dictModuleService.find("from DictAndModule where roleId = ?", DictAndModule.class, new String[]{model.getId()});
					for (DictAndModule rdm : list) {
						if (rdm.getModuleId().equals(str[0])) {
							
						}
					}*/
					dm.setModuleId(str[0]);
					dm.setDictId("operate" + str[i + 1]);
					System.out.println("dm:" + dm.toString());
					dictModuleService.saveOrUpdate(dm);
				}
			}

		}
		Set<Module> set = new HashSet<Module>();

		String[] ids = sb.toString().split(",");
		for (String moduleId : ids) {
			Module module = moduleService.get(Module.class, moduleId);
			set.add(module);
		}
		role.setModules(set);
		roleService.saveOrUpdate(role);
		return "alist";
	}

}
