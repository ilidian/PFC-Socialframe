package org.pfc.socialframe.model;

public class Feed {
	private int type;
	private String pic;
	private String actor;
	private String msg;
	public Feed(int type,String pic, String actor, String msg){
		this.setType(type);
		this.setPic(pic);
		this.setActor(actor);
		this.setMsg(msg);
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getType() {
		return type;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getPic() {
		return pic;
	}
	public void setActor(String actor) {
		this.actor = actor;
	}
	public String getActor() {
		return actor;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getMsg() {
		return msg;
	}
}
