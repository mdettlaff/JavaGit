package mdettlaff.javagit.command.porcelain;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import mdettlaff.javagit.command.common.Arguments;
import mdettlaff.javagit.command.common.Command;
import mdettlaff.javagit.command.plumbing.RevList;
import mdettlaff.javagit.command.plumbing.RevParse;
import mdettlaff.javagit.common.ObjectId;
import mdettlaff.javagit.object.Commit;
import mdettlaff.javagit.object.Creator;
import mdettlaff.javagit.object.GitObject;
import mdettlaff.javagit.object.GitObjects;

public class Log implements Command {

	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private final RevList revList;
	private final RevParse revParse;
	private final GitObjects objects;

	public Log(RevList revList, RevParse revParse, GitObjects objects) {
		this.revList = revList;
		this.revParse = revParse;
		this.objects = objects;
	}

	@Override
	public void execute(Arguments args) throws IOException {
		String revision = args.getParameters().isEmpty() ? "HEAD" : args.getParameters().get(0);
		boolean showMerges = args.isOptionSet("no-merges");
		ObjectId id = revParse.execute(revision);
		List<ObjectId> ids = revList.execute(id, showMerges);
		execute(ids);
	}

	private void execute(List<ObjectId> ids) throws IOException {
		for (ObjectId id : ids) {
			GitObject object = objects.read(id);
			Commit commit = (Commit) object.getContent();
			String commitAsString = commitToString(commit, id);
			System.out.println(commitAsString);
		}
	}

	private String commitToString(Commit commit, ObjectId id) {
		StringBuilder builder = new StringBuilder();
		builder.append("commit ").append(id).append('\n');
		Creator author = commit.getAuthor();
		builder.append("Author:\t").append(author.getName()).append(' ');
		builder.append('<').append(author.getEmail()).append('>');
		builder.append('\n');
		String date = new SimpleDateFormat(DATE_FORMAT).format(author.getDate().toDate());
		builder.append("Date:\t").append(date).append(' ');
		builder.append(author.getTimezone()).append('\n');
		builder.append('\n');
		builder.append(commit.getMessage()).append('\n');
		return builder.toString();
	}
}
