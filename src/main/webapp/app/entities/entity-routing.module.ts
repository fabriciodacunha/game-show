import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'participante',
        data: { pageTitle: 'gameShowApp.participante.home.title' },
        loadChildren: () => import('./participante/participante.module').then(m => m.ParticipanteModule),
      },
      {
        path: 'questao',
        data: { pageTitle: 'gameShowApp.questao.home.title' },
        loadChildren: () => import('./questao/questao.module').then(m => m.QuestaoModule),
      },
      {
        path: 'edicao',
        data: { pageTitle: 'gameShowApp.edicao.home.title' },
        loadChildren: () => import('./edicao/edicao.module').then(m => m.EdicaoModule),
      },
      {
        path: 'resposta',
        data: { pageTitle: 'gameShowApp.resposta.home.title' },
        loadChildren: () => import('./resposta/resposta.module').then(m => m.RespostaModule),
      },
      {
        path: 'play',
        data: { pageTitle: 'gameShowApp.play.home.title' },
        loadChildren: () => import('./play/play.module').then(m => m.PlayModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
