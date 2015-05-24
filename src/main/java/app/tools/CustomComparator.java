package app.tools;

import java.util.Comparator;

import org.jinstagram.entity.users.feed.MediaFeedData;

public class CustomComparator implements Comparator<MediaFeedData> {

	@Override
	public int compare(org.jinstagram.entity.users.feed.MediaFeedData arg0,
			org.jinstagram.entity.users.feed.MediaFeedData arg1) {
		return arg1.getLikes().getCount() - arg0.getLikes().getCount();
	}
}