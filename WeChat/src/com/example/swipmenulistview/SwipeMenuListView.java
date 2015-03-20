package com.example.swipmenulistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Scroller;

public class SwipeMenuListView extends ListView {
	/**
	 * ��ǰ������ListView��position
	 */
	private int slidePosition;
	/**
	 * ��ָ����X������
	 */
	private int downY;
	/**
	 * ��ָ����Y������
	 */
	private int downX;
	/**
	 * ��Ļ���
	 */
//	private int screenWidth;
	/**
	 * ListView��item
	 */
	private View itemView;
	/**
	 * ������
	 */
	private Scroller scroller;
	private static final int SNAP_VELOCITY = 600;
	/**
	 * �ٶ�׷�ٶ���
	 */
	private VelocityTracker velocityTracker;
	/**
	 * �Ƿ���Ӧ������Ĭ��Ϊ����Ӧ
	 */
	private boolean isSlide = false;
	/**
	 * ��Ϊ���û���������С����
	 */
	private int mTouchSlop;
	/**
	 *  �Ƴ�item��Ļص��ӿ�
	 */
	private OnItemRemovedListener onItemRemovedListener;
	/**
	 * ����ָʾitem������Ļ�ķ���,�����������,��һ��ö��ֵ�����
	 */
	private SlideDirection removeDirection;

	// ����ɾ�������ö��ֵ
	public enum SlideDirection {
		RIGHT, LEFT;
	}


	public SwipeMenuListView(Context context) {
		this(context, null);
	}

	public SwipeMenuListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SwipeMenuListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
//		screenWidth = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
		scroller = new Scroller(context);
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}
	
	/**
	 * �ַ��¼�����Ҫ�������жϵ�������Ǹ�item, �Լ�ͨ��postDelayed��������Ӧ���һ����¼�
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			// ����scroller������û�н���������ֱ�ӷ���
			if (!scroller.isFinished()) {
				return super.dispatchTouchEvent(event);
			}
			addVelocityTracker(event);

			downX = (int) event.getX();
			downY = (int) event.getY();

			slidePosition = pointToPosition(downX, downY);

			// ��Ч��position, �����κδ���
			if (slidePosition == AdapterView.INVALID_POSITION) {
				return super.dispatchTouchEvent(event);
			}

			// ��ȡ���ǵ����item view
			itemView = getChildAt(slidePosition - getFirstVisiblePosition());
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			if (Math.abs(getXVelocity()) > SNAP_VELOCITY
					|| (Math.abs(event.getX() - downX) > mTouchSlop && Math
							.abs(event.getY() - downY) < mTouchSlop)) {
				isSlide = true;
			}
			break;
		}
		case MotionEvent.ACTION_UP:
			recycleVelocityTracker();
			break;
		}

		return super.dispatchTouchEvent(event);
	}

	/**
	 * ���һ�����getScrollX()���ص������Ե�ľ��룬������View���ԵΪԭ�㵽��ʼ�����ľ��룬�������ұ߻���Ϊ��ֵ
	 */
	private void scrollRight() {
		removeDirection = SlideDirection.RIGHT;
		final int delta = (getWidth() + itemView.getScrollX());
		// ����startScroll����������һЩ�����Ĳ�����������computeScroll()�����е���scrollTo������item
		scroller.startScroll(itemView.getScrollX(), 0, -delta, 0,
				Math.abs(delta));
		postInvalidate(); // ˢ��itemView
	}

	/**
	 * ���󻬶���������������֪�����󻬶�Ϊ��ֵ
	 */
	private void scrollLeft() {
		removeDirection = SlideDirection.LEFT;
		final int delta = (getWidth() - itemView.getScrollX());
		// ����startScroll����������һЩ�����Ĳ�����������computeScroll()�����е���scrollTo������item
		scroller.startScroll(itemView.getScrollX(), 0, delta, 0,
				Math.abs(delta));
		postInvalidate(); // ˢ��itemView
	}

	/**
	 * ������ָ����itemView�ľ������ж��ǹ�������ʼλ�û�������������ҹ���
	 */
	private void scrollByDistanceX() {
		// �����������ľ��������Ļ������֮һ��������ɾ��
		if (itemView.getScrollX() >= getWidth() / 3) {
			scrollLeft();
		} else if (itemView.getScrollX() <= -getWidth() / 3) {
			scrollRight();
		} else {
			// ���ص�ԭʼλ��,Ϊ��͵����������ֱ�ӵ���scrollTo����
			itemView.scrollTo(0, 0);
		}

	}

	/**
	 * ���������϶�ListView item���߼�
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (isSlide && slidePosition != AdapterView.INVALID_POSITION) {
			addVelocityTracker(ev);
			final int action = ev.getAction();
			int x = (int) ev.getX();
			switch (action) {
			case MotionEvent.ACTION_MOVE:
				int deltaX = downX - x;
				downX = x;

				// ��ָ�϶�itemView����, deltaX����0���������С��0���ҹ�
				itemView.scrollBy(deltaX, 0);
				break;
			case MotionEvent.ACTION_UP:
				int velocityX = getXVelocity();
				if (velocityX > SNAP_VELOCITY) {
					scrollRight();
				} else if (velocityX < -SNAP_VELOCITY) {
					scrollLeft();
				} else {
					scrollByDistanceX();
				}

				recycleVelocityTracker();
				// ��ָ�뿪��ʱ��Ͳ���Ӧ���ҹ���
				isSlide = false;
				break;
			}

			return true; // �϶���ʱ��ListView������
		}

		//����ֱ�ӽ���ListView������onTouchEvent�¼�
		return super.onTouchEvent(ev);
	}

	@Override
	public void computeScroll() {
		// ����startScroll��ʱ��scroller.computeScrollOffset()����true��
		if (scroller.computeScrollOffset()) {
			// ��ListView item���ݵ�ǰ�Ĺ���ƫ�������й���
			itemView.scrollTo(scroller.getCurrX(), scroller.getCurrY());
			
			postInvalidate();

			// ��������������ʱ����ûص��ӿ�
			if (scroller.isFinished()) {
				if (onItemRemovedListener == null) {
					throw new NullPointerException("OnItemRemovedListener is null, we should called setOnItemRemovedListener()");
				}
				
				itemView.scrollTo(0, 0);
				onItemRemovedListener.onItemRemoved(removeDirection, slidePosition);
			}
		}
	}

	/**
	 * ����û����ٶȸ�����
	 * 
	 * @param event
	 */
	private void addVelocityTracker(MotionEvent event) {
		if (velocityTracker == null) {
			velocityTracker = VelocityTracker.obtain();
		}
		velocityTracker.addMovement(event);
	}

	/**
	 * �Ƴ��û��ٶȸ�����
	 */
	private void recycleVelocityTracker() {
		if (velocityTracker != null) {
			velocityTracker.recycle();
			velocityTracker = null;
		}
	}

	/**
	 * ��ȡX����Ļ����ٶ�,����0���һ�������֮����
	 * 
	 * @return
	 */
	private int getXVelocity() {
		velocityTracker.computeCurrentVelocity(1000);
		int velocity = (int) velocityTracker.getXVelocity();
		return velocity;
	}

	/**
	 * ���û���ɾ���Ļص��ӿ�
	 * @param removeListener
	 */
	public void setOnItemRemovedListener(OnItemRemovedListener onItemRemovedListener) {
		this.onItemRemovedListener = onItemRemovedListener;
	}
	
	/**
	 * 
	 * ��ListView item������Ļ���ص�����ӿ�
	 * ������Ҫ�ڻص�����removeItem()���Ƴ���Item,Ȼ��ˢ��ListView
	 * 
	 *
	 */
	public interface OnItemRemovedListener {
		public void onItemRemoved(SlideDirection direction, int position);
	}

}

