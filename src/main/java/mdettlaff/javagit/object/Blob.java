package mdettlaff.javagit.object;

public class Blob implements ObjectContent {

	private final byte[] content;

	public Blob(byte[] content) {
		this.content = content;
	}

	public byte[] getContent() {
		return content;
	}

	@Override
	public byte[] toByteArray() {
		return content;
	}

	@Override
	public String toString() {
		return new String(content);
	}
}
