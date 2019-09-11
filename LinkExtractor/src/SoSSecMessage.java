class SoSSecMessage {
	private String name;
	private SoSSecBehaviour receiver;
	private SoSSecBehaviour sender;

	public SoSSecMessage() {
		super();
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// set receiver
	public SoSSecBehaviour getReceiver() {
		return receiver;
	}

	public void setReceiver(SoSSecBehaviour receiver) {
		this.receiver = receiver;
	}

	// set sender
	public SoSSecBehaviour getSender() {
		return sender;
	}

	public void setSender(SoSSecBehaviour sender) {
		this.sender = sender;
	}
}
