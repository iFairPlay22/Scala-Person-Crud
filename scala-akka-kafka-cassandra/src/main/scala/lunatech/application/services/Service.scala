package lunatech.application.services

trait Service {
  def recoverFunction[T](implicit system_error: Throwable, view_error: Throwable): T = {
    system_error.printStackTrace()
    throw view_error
  }
}
