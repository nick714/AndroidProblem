package cn.thinker.wechatmomentsdemo.core;

public interface IExecutors {
    /**
     * Handle the task on local cache within a public thread.
     * @param task the task to execute
     */
    boolean executeOnLocal(ITask<?> task);

    /**
     * Handle the task via Volley.
     * @param task the task to execute
     */
    boolean executeOnVolley(ITask<?> task);


    /**
     * Handle the task serially on local then on network
     * @param task the task to execute
     */
    @SuppressWarnings("unused")
    boolean execute(ITask<?> task);
}
