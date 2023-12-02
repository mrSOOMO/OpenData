package com.soomo.opendata.service.util.console

import zio.{ZIO, *}

import java.io.IOException

/**
 * Prints a message within a border frame to the console. The printed text will
 * be bolded.
 *
 * @example
 *   {{{
 * printWithFrame("Hello World")
 *   }}}
 *   will output:
 *   {{{
 * ================
 * | Hello World |
 * ================
 *   }}}
 *
 * @param message
 *   The message string to be printed within the frame.
 * @return
 *   A ZIO effect that prints the message to the console. The effect may fail
 *   with an IOException if there is an error during execution.
 */

def printWithFrame(message: String): ZIO[Any, IOException, Unit] = {
  val frameLine = "=" * (message.length + 4)
  val BOLD      = "\u001B[1m"
  val RESET     = "\u001B[0m"
  for {
    _ <- Console.printLine(BOLD + frameLine + RESET)
    _ <- Console.printLine(BOLD + s"| $message |" + RESET)
    _ <- Console.printLine(BOLD + frameLine + RESET)
  } yield ()
}
