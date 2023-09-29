/***************************************************************************************
* Copyright (c) 2020-2023 Institute of Computing Technology, Chinese Academy of Sciences
*
* DiffTest is licensed under Mulan PSL v2.
* You can use this software according to the terms and conditions of the Mulan PSL v2.
* You may obtain a copy of Mulan PSL v2 at:
*          http://license.coscl.org.cn/MulanPSL2
*
* THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
* EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
* MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
*
* See the Mulan PSL v2 for more details.
***************************************************************************************/

import mill._, scalalib._, scalafmt._
import coursier.maven.MavenRepository

trait CommonModule extends ScalaModule {
  override def scalaVersion = "2.13.10"
  override def scalacPluginIvyDeps = Agg(ivy"org.chipsalliance:::chisel-plugin:5.0.0")
  override def scalacOptions = super.scalacOptions() ++ Agg("-Ymacro-annotations", "-Ytasty-reader")
}

trait HasChisel extends ScalaModule {
  override def ivyDeps = Agg(ivy"org.chipsalliance::chisel:5.0.0")
}

object difftest extends SbtModule with ScalafmtModule with CommonModule with HasChisel {
  
  override def millSourcePath = millOuterCtx.millSourcePath
}

object chiselModule extends SbtModule with ScalafmtModule with CommonModule with HasChisel {
  override def millSourcePath = millOuterCtx.millSourcePath

  override def moduleDeps = super.moduleDeps ++ Seq(
    difftest
  )

  object test extends SbtModuleTests with TestModule.ScalaTest {
    override def ivyDeps = super.ivyDeps() ++ Agg(
      ivy"org.scalatest::scalatest:3.2.4",
      ivy"edu.berkeley.cs::chisel-iotesters:2.5+"
    )
    def testFrameworks = "org.scalatest.tools.Framework"
  }
}
