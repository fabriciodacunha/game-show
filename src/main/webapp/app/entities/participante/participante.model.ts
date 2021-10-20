import * as dayjs from 'dayjs';
import { IResposta } from 'app/entities/resposta/resposta.model';
import { IPlay } from 'app/entities/play/play.model';

export interface IParticipante {
  id?: number;
  carastroUsuario?: string | null;
  participanteNome?: string | null;
  participanteEmail?: string | null;
  participanteDataDeNascimento?: dayjs.Dayjs | null;
  respostas?: IResposta[] | null;
  play?: IPlay | null;
}

export class Participante implements IParticipante {
  constructor(
    public id?: number,
    public carastroUsuario?: string | null,
    public participanteNome?: string | null,
    public participanteEmail?: string | null,
    public participanteDataDeNascimento?: dayjs.Dayjs | null,
    public respostas?: IResposta[] | null,
    public play?: IPlay | null
  ) {}
}

export function getParticipanteIdentifier(participante: IParticipante): number | undefined {
  return participante.id;
}
